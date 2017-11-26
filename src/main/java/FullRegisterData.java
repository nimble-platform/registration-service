/**
 * Created by evgeniyh on 11/22/17.
 */
public class FullRegisterData {
    private String firstName;
    private String lastName;
    private String userEmail;
    private String userPassword;
    private String companyName;
    private String country;
    private String city;
    private String street;
    private String building;
    private String postalCode;

    public FullRegisterData(String firstName, String lastName, String userEmail, String userPassword, String companyName, String country, String city, String street, String building, String postalCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.companyName = companyName;
        this.country = country;
        this.city = city;
        this.street = street;
        this.building = building;
        this.postalCode = postalCode;
    }

    public boolean hasAllValues() {
        return firstName != null &&
                lastName != null &&
                userEmail != null &&
                userPassword != null &&
                companyName != null &&
                country != null &&
                city != null &&
                street != null &&
                building != null &&
                postalCode != null;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getBuilding() {
        return building;
    }

    public String getPostalCode() {
        return postalCode;
    }
}
