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

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

/**
 * Created by evgeniyh on 11/19/17.
 */
public class Common {
    private static String BASE_URL = "https://nimble-platform.salzburgresearch.at/nimble/identity";

    static String LOGIN_URL = BASE_URL + "/login";
    static String userRegisterUrl = BASE_URL + "/register/user";
    static String COMPANY_REGISTER_URL = BASE_URL + "/register/company";

    static String getInputStreamAsString(InputStream stream) throws IOException {
        StringWriter writer = new StringWriter();
        IOUtils.copy(stream, writer, "UTF-8");
        return writer.toString();
    }

    static String sendPostCommand(String url, String content, String accessToken) {
        CloseableHttpResponse response = null;
        String responseString = null;

        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("Authorization", "Bearer " + accessToken);
            HttpEntity entity = new ByteArrayEntity(content.getBytes("UTF-8"));
            httpPost.setEntity(entity);

            response = httpclient.execute(httpPost);
            entity = response.getEntity();
            responseString = EntityUtils.toString(entity, "UTF-8");

            System.out.println(String.format("Status=%s, response=%s", response.getStatusLine(), responseString));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return responseString;
    }


    public static String getKeyFromJsonString(String key, String jsonString) {
        JsonObject user = (JsonObject) new JsonParser().parse(jsonString);
        if (user != null) {
            return user.get(key).getAsString();
        }
        throw new RuntimeException(String.format("Missing key '%s' in json string '%s'", key, jsonString));
    }
}
