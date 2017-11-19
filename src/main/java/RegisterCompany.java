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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

@WebServlet("/registerCompany")
public class RegisterCompany extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Inside do post on the /registerCompany path");
        InputStream fileStream = request.getInputStream();
        StringWriter writer = new StringWriter();
        IOUtils.copy(fileStream, writer, "UTF-8");
        String inputString = writer.toString();
        System.out.println(inputString);
        JsonObject company = (JsonObject)new JsonParser().parse(inputString);
        JsonObject credentials = company.getAsJsonObject("credentials");
        company.remove("credentials");

        String loggedUser = Common.sendPostCommand(Common.USER_LOGIN_URL, credentials.toString(), "");
        String accessToken = Common.getKeyFromJsonString("accessToken", loggedUser);

        String registeredCompany = Common.sendPostCommand(Common.COMPANY_REGISTER_URL, company.toString(), accessToken);
        System.out.println("Successfully registered the company - " + registeredCompany);
        response.getWriter().write(registeredCompany);
        response.setStatus(200);
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
