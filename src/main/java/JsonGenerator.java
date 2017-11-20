/**
 * Created by evgeniyh on 11/20/17.
 */
public class JsonGenerator {
    private static String CREDENTIALS_TEMPLATE = "{ \"username\": \"%s\", \"password\": \"%s\" } ";

    private static String USER_JSON_TEMPLATE = "{ \"user\": { \"username\": \"%s\", \"firstname\": \"%s\", \"lastname\": \"%s\", \"email\": \"%s\" }," +
            "\"credentials\": { \"username\": \"%s\", \"password\": \"%s\" } }";

    private static String COMPANY_JSON_TEMPLATE = "{\"name\": \"%s\", \"userID\": %s, " +
            " \"address\": {\"country\": \"%s\", \"cityName\": \"%s\", \"streetName\": \"%s\", \"buildingNumber\": \"%s\", \"postalCode\": \"%s\"} }";

    //    public static String COMPANY_JSON_TEMPLATE = "{\"name\": \"%s\", \"userID\": %s, " +
//            " \"address\": {\"country\": \"%s\", \"cityName\": \"%s\", \"streetName\": \"%s\", \"buildingNumber\": \"%s\", \"postalCode\": \"%s\"}," +
//            " \"credentials\": %s }";

    static String createCredentialsJson(String username, String password) {
        return String.format(CREDENTIALS_TEMPLATE, username, password);
    }

    static String createUserJson(String username, String firstName, String lastName, String email, String password) {
        return String.format(JsonGenerator.USER_JSON_TEMPLATE, username, firstName, lastName, email, email, password);
    }

    static String createCompanyJson(String companyName, String userId, String country, String city, String street, String building, String postalCode) {
        return String.format(COMPANY_JSON_TEMPLATE, companyName, userId, country, city, street, building, postalCode);
    }
}
