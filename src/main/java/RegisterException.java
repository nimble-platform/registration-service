/**
 * Created by evgeniyh on 11/26/17.
 */
public class RegisterException extends Exception {
    private final int errorCode;

    public RegisterException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
