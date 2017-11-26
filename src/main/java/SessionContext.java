/**
 * Created by evgeniyh on 11/20/17.
 */
public class SessionContext {
    private String cookie;
    private String accessToken;

    public SessionContext(String cookie, String accessToken) {
        this.cookie = cookie;
        this.accessToken = accessToken;
    }

    public String getCookie() {
        return cookie;
    }

    public String getAccessToken() {
        return accessToken;
    }


    @Override
    public String toString() {
        return String.format("cookie=%s, accessToken=%s", cookie, accessToken);
    }
}
