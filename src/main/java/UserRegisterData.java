/**
 * Created by evgeniyh on 11/26/17.
 */
public class UserRegisterData {
    private String firstname;
    private String lastname;
    private String email;
//    private String password;
    private String userID;

    public UserRegisterData(String firstname, String lastname, String email, String userID) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
//        this.password = password;
        this.userID = userID;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

//    public String getPassword() {
//        return password;
//    }

    public String getUserID() {
        return userID;
    }
}
