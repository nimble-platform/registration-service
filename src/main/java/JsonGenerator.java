/**
 * Created by evgeniyh on 11/20/17.
 */
public class JsonGenerator {
    private static String CREDENTIALS_TEMPLATE = "{ " +
            "\"username\": \"%s\"," +
            "\"password\": \"%s\" } ";

    private static String USER_JSON_TEMPLATE = "{ " +
            "\"user\": { " +
                "\"firstname\": \"%s\"," +
                "\"lastname\": \"%s\"," +
                "\"email\": \"%s\" }, " +
            "\"credentials\": %s }";

    private static String COMPANY_JSON_TEMPLATE = "{" +
            "\"name\": \"%s\"," +
            "\"userID\": %s," +
            "\"address\": {" +
                "\"country\": \"%s\"," +
                "\"cityName\": \"%s\"," +
                "\"streetName\": \"%s\"," +
                "\"buildingNumber\": \"%s\"," +
                "\"postalCode\": \"%s\"} " +
            "}";

    static String createCredentialsJson(String username, String password) {
        return String.format(CREDENTIALS_TEMPLATE, username, password);
    }

    static String createUserJson(String firstName, String lastName, String email, String password) {
        String credentials = createCredentialsJson(email, password);
        return String.format(USER_JSON_TEMPLATE, firstName, lastName, email, credentials);
    }

    static String createCompanyJson(String companyName, String userId, String country, String city, String street, String building, String postalCode) {
        return String.format(COMPANY_JSON_TEMPLATE, companyName, userId, country, city, street, building, postalCode);
    }
}
