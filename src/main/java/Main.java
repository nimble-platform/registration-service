/**
 * Created by evgeniyh on 11/16/17.
 */
public class Main {
    private static String[] USERNAMES_FOR_COMANIES = {
            "Pro_vernis_user",
            "Vicbar_user",
            "Químicas_Tovar_user",
            "Schindler_Holzplatte_user",
            "Interboard_user",
            "Tableros_Garrido_user",
            "Meditrans_user",
            "Hoffman_Transport_user",
            "Hase_Logistik_user",

    };

    private static String[] EMAILS_FOR_USERS = {
            "user@Pro_vernis.com",
            "user@Vicbar.com",
            "user@Químicas_Tovar.com",
            "user@Schindler_Holzplatte.com",
            "user@Interboard.com",
            "user@Tableros_Garrido.com",
            "user@Meditrans.com",
            "user@Hoffman_Transport.com",
            "user@Hase_Logistik.com",
    };

    private static String[] COMPANY_NAMES = {
            "Pro vernis",
            "Vicbar",
            "Químicas Tovar",
            "Schindler Holzplatte",
            "Interboard",
            "Tableros Garrido",
            "Meditrans",
            "Hoffman Transport",
            "Hase Logistik",
    };


    private static String[] COMPANY_COUNTRIES = {
            "France",
            "Spain",
            "Spain",
            "Austria",
            "Spain",
            "Spain",
            "Spain",
            "Germany",
            "Austria",
    };

    private static String[] COMPANY_CITIES = {
            "Paris",
            "Valencia",
            "Valencia",
            "Gmunden",
            "Valencia",
            "Valencia",
            "Valencia",
            "Munich",
            "Graz",
    };

    private static String[] COMPANY_STREETS = {
            "Rue de Tolbiac",
            "Calle Rue del Percebe",
            "Carrer del segadors",
            "Badgasse",
            "Calle Benjamin Franklin",
            "Carrer de la seu",
            "Pol ind la reva",
            "Finkenstrasse",
            "Moserhofgasse",
    };

    private static String[] COMPANY_BUILDINGS = {
            "33",
            "12",
            "2",
            "3",
            "18",
            "24",
            "NONE",
            "41",
            "31",

    };

    private static String POSTAL_CODE = "NONE (update me)";
    private static String FIRST_NAME = "FIRST_NAME (update me)";
    private static String LAST_NAME = "LAST_NAME (update me)";
    private static String PASSWORD = "MY_VERY_STRONG_PASSWORD";

    private static String CREDENTIALS_TEMPLATE = "{ \"username\": \"%s\", \"password\": \"%s\" } ";

    private static String USER_JSON_TEMPLATE = "{ \"user\": { \"username\": \"%s\", \"firstname\": \"%s\", \"lastname\": \"%s\", \"email\": \"%s\" }," +
            "\"credentials\": { \"username\": \"%s\", \"password\": \"%s\" } }";

    private static String COMPANY_JSON_TEMPLATE = "{\"name\": \"%s\", \"userID\": %s, " +
            " \"address\": {\"country\": \"%s\", \"cityName\": \"%s\", \"streetName\": \"%s\", \"buildingNumber\": \"%s\", \"postalCode\": \"%s\"}," +
            " \"credentials\": %s }";

    public static void main(String[] args) {
        for (int i = 0; i < 1; i++) {
            String companyName = COMPANY_NAMES[i];
            String email = EMAILS_FOR_USERS[i];
            out(String.format("Running register a user for company '%s'", companyName));
            String userToRegister = String.format(USER_JSON_TEMPLATE, USERNAMES_FOR_COMANIES[i], FIRST_NAME, LAST_NAME, email, email, PASSWORD);
            String createdUser = Common.sendPostCommand(Common.USER_REGISTER_URL, userToRegister, "");
            if (createdUser == null) {
                out("Error during registration");
                System.exit(1);
            }

            String userId = Common.getKeyFromJsonString("userID", createdUser);
            out(String.format("SUCCESS !!! User named '%s' for company '%s' has the id '%s'", USERNAMES_FOR_COMANIES[i], companyName, userId));

//        String userId = "476";

            out(String.format("Running register a company '%s' with user '%s'", companyName, email));

            String credentials = String.format(CREDENTIALS_TEMPLATE, email, PASSWORD);
            String companyToRegister = String.format(COMPANY_JSON_TEMPLATE, companyName, userId, COMPANY_COUNTRIES[i], COMPANY_CITIES[i], COMPANY_STREETS[i], COMPANY_BUILDINGS[i], POSTAL_CODE, credentials);

            String registeredCompany = Common.sendPostCommand("http://localhost:999/registerCompany", companyToRegister, "");
            if (registeredCompany == null) {
                out("Error during registration");
                System.exit(1);
            }
            String companyId = Common.getKeyFromJsonString("companyID", registeredCompany);
            out(String.format("SUCCESS !!! Registered company '%s' by user id '%s', company id is '%s'", companyName, userId, companyId));
        }
    }


    private static void out(String s) {
        System.out.println(s);
    }
}
