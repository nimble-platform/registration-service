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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;

@WebServlet("/registerUser")
public class RegisterUser extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Inside do post on the /registerUser path");
//        String inputString = Common.getInputStreamAsString(request.getInputStream());

//        System.out.println(inputString);
//        JsonObject user = (JsonObject) new JsonParser().parse(inputString);

//        TODO: validate required fields

//        String registeredUser = Common.sendPostCommand(Common.USER_REGISTER_URL, inputString, "");
//
//        System.out.println("Successfully registered the user - " + registeredUser);
//        response.getWriter().write(registeredUser);
//        response.setStatus(200);
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
