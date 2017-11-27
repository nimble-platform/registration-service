import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;
import org.apache.http.*;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by evgeniyh on 11/19/17.
 */
public class Common {
    private static String BASE_URL = "https://nimble-platform.salzburgresearch.at/nimble/identity";
    private static String USER_LOGIN_URL = BASE_URL + "/login";
    private static String USER_REGISTER_URL = BASE_URL + "/register/user";
    private static String COMPANY_REGISTER_URL = BASE_URL + "/register/company";

    private static int REQUIRED_EXCEL_COLUMNS = 10;

    static String getInputStreamAsString(InputStream stream) throws IOException {
        StringWriter writer = new StringWriter();
        IOUtils.copy(stream, writer, "UTF-8");
        return writer.toString();
    }

    static SessionContext loginUser(String username, String password) {
        CloseableHttpResponse response = null;
        try {
            String credentials = JsonGenerator.createCredentialsJson(username, password);
            response = sendPostCommand(USER_LOGIN_URL, credentials);

            String responseString = logAndGetResponse(response);

            String accessToken = getKeyFromJsonString("accessToken", responseString);
            String cookie = response.getFirstHeader("Set-Cookie").getValue();

            return new SessionContext(cookie, accessToken);
        } finally {
            closeResponse(response);
        }
    }

    static UserRegisterData registerUser(String jsonUser) {
        CloseableHttpResponse response = null;
        try {
            response = sendPostCommand(USER_REGISTER_URL, jsonUser);

            String responseString = logAndGetResponse(response);
            return (new Gson()).fromJson(responseString, UserRegisterData.class);
        } finally {
            closeResponse(response);
        }
    }

    static String registerCompany(String jsonCompany, SessionContext sessionContext) {
        CloseableHttpResponse response = null;
        try {
            Header tokenHeader = new BasicHeader("Authorization", "Bearer " + sessionContext.getAccessToken());
            Header cookieHeader = new BasicHeader("Cookie", sessionContext.getCookie());
            response = sendPostCommand(COMPANY_REGISTER_URL, jsonCompany, tokenHeader, cookieHeader);

            return logAndGetResponse(response);
        } finally {
            closeResponse(response);
        }
    }

    static String getKeyFromJsonString(String key, String jsonString) {
        JsonObject user = (JsonObject) new JsonParser().parse(jsonString);
        if (user != null) {
            return user.get(key).getAsString();
        }
        throw new RuntimeException(String.format("Missing key '%s' in json string '%s'", key, jsonString));
    }

    static List<FullRegisterData> createRegistrationDataList(InputStream inputStream) throws Exception {
        List<FullRegisterData> list = new LinkedList<>();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet companiesSheet = workbook.getSheet("Companies");
        if (companiesSheet == null) {
            companiesSheet = workbook.getSheetAt(0);
        }
        if (!excelHasAllValues(companiesSheet.getRow(0))) {
            throw new RuntimeException("Failed to parse the excel file");
        }
        int rowCount = companiesSheet.getPhysicalNumberOfRows();
        if (rowCount < 1) {
            throw new RegisterException("Excel file must have at least one row of data", HttpServletResponse.SC_BAD_REQUEST);
        }

        System.out.println("Excel file row count - " + rowCount);

        boolean reachedEmptyLine = false;
        for (int i = 1; i < rowCount; i++) {
            System.out.println("Parsing excel file, row number - " + i);
            Row row = companiesSheet.getRow(i);

            for (int j = 0; j < REQUIRED_EXCEL_COLUMNS; j++) {
                Cell currentCell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

                CellType type = currentCell.getCellTypeEnum();
                if (type == CellType.BLANK) {  // Check for when reaching an empty row
                    if (isAllRowEmpty(row)) {
                        reachedEmptyLine = true;
                        break;
                    } else {
                        throw new RegisterException("Error parsing excel file - line has empty cells", HttpServletResponse.SC_BAD_REQUEST);
                    }
                }

                if (type != CellType.STRING && type != CellType.NUMERIC) {
                    throw new RuntimeException(type + " is not supported");
                }
            }
            if (reachedEmptyLine) {
                System.out.println("Stopping file parsing - reached empty row on row count - " + rowCount);
                break;
            }
            String firstName = row.getCell(0).getStringCellValue();
            String lastName = row.getCell(1).getStringCellValue();
            String email = row.getCell(2).getStringCellValue();
            String password = row.getCell(3).getStringCellValue();
            String companyName = row.getCell(4).getStringCellValue();
            String country = row.getCell(5).getStringCellValue();
            String city = row.getCell(6).getStringCellValue();
            String street = row.getCell(7).getStringCellValue();
            String building = String.valueOf(row.getCell(8).getNumericCellValue());
            String postalCode = row.getCell(9).getStringCellValue();

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || companyName.isEmpty() ||
                    country.isEmpty() || city.isEmpty() || street.isEmpty() || building.isEmpty() || postalCode.isEmpty()) {
                throw new RuntimeException("Values inside the excel file can't be empty");
            }

            list.add(new FullRegisterData(firstName, lastName, email, password, companyName, country, city, street, building, postalCode));
        }
        return list;
//                System.out.println(firstName);
//                System.out.println(lastName);
//                System.out.println(email);
//                System.out.println(password);
//                System.out.println(companyName);
//                System.out.println(country);
//                System.out.println(city);
//                System.out.println(street);
//                System.out.println(building);
//                System.out.println(postalCode);
    }

    private static boolean isAllRowEmpty(Row row) {
        boolean isAllEmpty = true;
        for (int i = 0; i < REQUIRED_EXCEL_COLUMNS; i++) {
            Cell c = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            if (CellType.BLANK != c.getCellTypeEnum()) {
                isAllEmpty = false;
            }
        }
        return isAllEmpty;
    }

    private static boolean excelHasAllValues(Row row) {
//        System.out.println(row.getCell(0).getStringCellValue());
//        System.out.println(row.getCell(1).getStringCellValue());
//        System.out.println(row.getCell(2).getStringCellValue());
//        System.out.println(row.getCell(3).getStringCellValue());
//        System.out.println(row.getCell(4).getStringCellValue());
//        System.out.println(row.getCell(5).getStringCellValue());
//        System.out.println(row.getCell(6).getStringCellValue());
//        System.out.println(row.getCell(7).getStringCellValue());
//        System.out.println(row.getCell(8).getStringCellValue());
//        System.out.println(row.getCell(9).getStringCellValue());

        return row.getCell(0).getStringCellValue().equals("UserFirstName") &&
                row.getCell(1).getStringCellValue().equals("UserLastName") &&
                row.getCell(2).getStringCellValue().equals("UserEmail") &&
                row.getCell(3).getStringCellValue().equals("UserPassword") &&
                row.getCell(4).getStringCellValue().equals("CompanyName") &&
                row.getCell(5).getStringCellValue().equals("Country") &&
                row.getCell(6).getStringCellValue().equals("City") &&
                row.getCell(7).getStringCellValue().equals("Street") &&
                row.getCell(8).getStringCellValue().equals("Building") &&
                row.getCell(9).getStringCellValue().equals("PostalCode");
    }

    private static String logAndGetResponse(HttpResponse response) {
        try {
            String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");

            System.out.println(String.format("Status=%s, Response=%s", response.getStatusLine().toString(), responseString));

            return responseString;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void closeResponse(CloseableHttpResponse response) {
        if (response != null) {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static CloseableHttpResponse sendPostCommand(String url, String content, Header... headers) {
        CloseableHttpResponse response = null;

        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-type", "application/json");
            for (Header h : headers) {
                httpPost.setHeader(h);
            }
            HttpEntity entity = new ByteArrayEntity(content.getBytes("UTF-8"));
            httpPost.setEntity(entity);

            response = httpclient.execute(httpPost);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return response;
    }
}
