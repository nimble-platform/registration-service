/**
 * Copyright 2015, 2016 IBM Corp. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@MultipartConfig
@WebServlet("/register")
public class Register extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Inside do post on the /register path");

//        Enumeration<String> headers = request.getHeaderNames();
//        while (headers.hasMoreElements()) {
//            String h = headers.nextElement();
//            String s = request.getHeader(h);
//            System.out.println(String.format("%s = %s", h, s));
//        }
        try {
            List<FullRegisterData> registerList = getRegisterDataList(request);
            if (registerList == null) {
                logAndSetResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Failed to parse the excel file\n");
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            for (FullRegisterData data : registerList) {
                String singleRegistration = fullRegisterFlow(data);
                stringBuilder.append(singleRegistration);
            }
            logAndSetResponse(response, HttpServletResponse.SC_OK, stringBuilder.toString());
        } catch (RegisterException e) {
            e.printStackTrace();
            logAndSetResponse(response, e.getErrorCode(), e.getMessage() + "\n");
        } catch (Exception e) {
            e.printStackTrace();
            logAndSetResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Error during register command : " + e.getMessage() + "\n");
        }
    }


    private List<FullRegisterData> getRegisterDataList(HttpServletRequest request) throws Exception {
        String content = request.getHeader("content-type");
        if (content.startsWith("multipart/form-data")) {
            Part p = request.getPart("excel-file");
            if (p == null) {
                throw new RegisterException("use 'excel-file' header when sending the file", HttpServletResponse.SC_BAD_REQUEST);
            }

            return Common.createRegistrationDataList(p.getInputStream());
        } else { // Default for now
            String inputString = Common.getInputStreamAsString(request.getInputStream());
            if (inputString == null || inputString.isEmpty()) {
                throw new RegisterException("Body can't be empty", HttpServletResponse.SC_PARTIAL_CONTENT);
            }

            System.out.println("Received input : " + inputString);
            final FullRegisterData registerData = (new Gson()).fromJson(inputString, FullRegisterData.class);
            return new LinkedList<FullRegisterData>() {{
                add(registerData);
            }};
        }
    }


    private String fullRegisterFlow(FullRegisterData registerData) throws RegisterException {
        try {
            if (!registerData.hasAllValues()) {
                throw new RegisterException("Missing values from the template", HttpServletResponse.SC_PARTIAL_CONTENT);
            }

            String userEmail = registerData.getUserEmail();
            String userPassword = registerData.getUserPassword();
            String companyName = registerData.getCompanyName();

            String userToRegister = JsonGenerator.createUserJson(registerData.getFirstName(), registerData.getLastName(), userEmail, userPassword);
            UserRegisterData user = Common.registerUser(userToRegister);
            String userId = user.getUserID();
            if (userId == null || userId.isEmpty()) {
                throw new RuntimeException("Error during registration - registered user id returned null");
            }

            System.out.println(String.format("SUCCESS !!! User with email '%s' from company '%s' has the id '%s'", userEmail, companyName, userId));
            SessionContext sessionContext = Common.loginUser(userEmail, userPassword);

            String companyJson = JsonGenerator.createCompanyJson(companyName, userId, registerData.getCountry(), registerData.getCity(), registerData.getStreet(), registerData.getBuilding(), registerData.getPostalCode());
            String registeredCompany = Common.registerCompany(companyJson, sessionContext);

            String companyId = Common.getKeyFromJsonString("companyID", registeredCompany);

            return String.format("User registered id='%s', Company registered id='%s'\n", userId, companyId);
        } catch (Exception ex) {
            throw new RegisterException("Error during register command : " + ex.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void logAndSetResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
        System.out.print(message);
        response.getWriter().write(message);
        response.setStatus(statusCode);
    }

//
//    String userJsonObj = "{\n" +
//            "    \"user\": {\n" +
//            "        \"companyID\": \"1111\",\n" +
//            "        \"companyName\": \"my-company\",\n" +
//            "        \"username\": \"testUser\",\n" +
//            "        \"firstname\": \"firstName\",\n" +
//            "        \"lastname\": \"lastName\",\n" +
//            "        \"userID\": 1122,\n" +
//            "        \"email\": \"email@test1.com\",\n" +
//            "        \"phoneNumber\": \"0500000000\",\n" +
//            "        \"placeOBirth\": \"earth\",\n" +
//            "        \"dateOfBirth\": \"2000-01-01\"\n" +
//            "    },\n" +
//            "    \"credentials\": {\n" +
//            "        \"username\": \"email@test1.com\",\n" +
//            "        \"password\": \"password\"\n" +
//            "    }\n" +
//            "}";
//    String companyJsonObj = "{\n" +
//            "    \"userID\": 83,\n" +
//            "    \"name\": \"my-new-company\",\n" +
//            "    \"companyID\": 1111,\n" +
//            "    \"address\": {\n" +
//            "        \"streetName\": \"test-street\",\n" +
//            "        \"buildingNumber\": \"32\",\n" +
//            "        \"cityName\": \"test-city\",\n" +
//            "        \"postalCode\": \"TEST001\",\n" +
//            "        \"country\": \"test-country\"\n" +
//            "    }\n" +
//            "}";
}
