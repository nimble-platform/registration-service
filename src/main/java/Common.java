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

import java.io.*;

/**
 * Created by evgeniyh on 11/19/17.
 */
public class Common {
    private static String BASE_URL = "https://nimble-platform.salzburgresearch.at/nimble/identity";
    private static String USER_LOGIN_URL = BASE_URL + "/login";
    private static String USER_REGISTER_URL = BASE_URL + "/register/user";
    private static String COMPANY_REGISTER_URL = BASE_URL + "/register/company";

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

    static JsonArray generateJsonsFromExcel(String path) {
        int arraySize = 0;
        JsonArray jsonsArray = new JsonArray();
        try {
            FileInputStream excelFile = new FileInputStream(new File(path));

            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet companiesSheet = workbook.getSheet("Companies");
            if (companiesSheet == null) {
                companiesSheet = workbook.getSheetAt(0);
            }
            for (Row currentRow : companiesSheet) {
                for (Cell currentCell : currentRow) {
                    CellType type = currentCell.getCellTypeEnum();
                    if (type == CellType.STRING) {
                        System.out.print(currentCell.getStringCellValue() + "--");
                    } else if (type == CellType.NUMERIC) {
                        System.out.print(currentCell.getNumericCellValue() + "--");
                    } else if (type == CellType.BLANK) {
                        System.out.print("**");
                    } else {
                        System.out.println(type + "NOT supported");
                    }
                }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    private static String logAndGetResponse(HttpResponse response) {
        try {
            String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");

            System.out.println(String.format("Status=%s, Response=%s", response.getStatusLine().toString(), response));

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
