/**
 * Created by evgeniyh on 11/26/17.
 */
public class UserRegisterData {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String userId;

    public UserRegisterData(String firstName, String lastName, String email, String password, String userId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUserId() {
        return userId;
    }
}
