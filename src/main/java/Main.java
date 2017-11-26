/**
 * Created by evgeniyh on 11/16/17.
 */
public class Main {
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

    public static void main(String[] args) {
//        Common.generateJsonsFromExcel("nimble_entities.xlsx");
        for (int i = 0; i < 1; i++) {
            String companyName = COMPANY_NAMES[i];
            String email = EMAILS_FOR_USERS[i];
            out(String.format("Running register a company '%s' with user '%s'", companyName, email));
            String userToRegister = JsonGenerator.createUserJson(FIRST_NAME, LAST_NAME, email, PASSWORD);
            String registeredUser = Common.registerUser(userToRegister);
            if (registeredUser == null) {
                out("Error during registration");
                System.exit(1);
            }

            String userId = Common.getKeyFromJsonString("userID", registeredUser);
            out(String.format("SUCCESS !!! User with email '%s' from company '%s' has the id '%s'", email, companyName, userId));

            SessionContext sessionContext = Common.loginUser(email, PASSWORD);
            String companyJson = JsonGenerator.createCompanyJson(companyName, userId, COMPANY_COUNTRIES[i], COMPANY_CITIES[i], COMPANY_STREETS[i], COMPANY_BUILDINGS[i], POSTAL_CODE);
            String registeredCompany = Common.registerCompany(companyJson, sessionContext);
            out(registeredCompany);

            String companyId = Common.getKeyFromJsonString("companyID", registeredCompany);
            out(String.format("SUCCESS !!! Registered company '%s' by user id '%s', company id is '%s'", companyName, userId, companyId));
        }
    }


    private static void out(String s) {
        System.out.println(s);
    }
}
