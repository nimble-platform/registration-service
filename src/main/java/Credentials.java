/**
 * Created by evgeniyh on 11/20/17.
 */
public class Credentials {
    private String cookie;
    private String accessToken;
    private String userID;

    public Credentials(String cookie, String accessToken, String userID) {
        this.cookie = cookie;
        this.accessToken = accessToken;
        this.userID = userID;
    }

    public String getCookie() {
        return cookie;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getUserID() {
        return userID;
    }

    @Override
    public String toString() {
        return String.format("userId=%s, cookie=%s, accessToken=%s", userID, cookie, accessToken);
    }
}
