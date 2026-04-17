package in.gov.bis.platform.common.exception;

public class ValidationException extends BisException {

    public ValidationException(String message) {
        super(message, "VALIDATION_ERROR", 400);
    }

    public ValidationException(String message, String errorCode) {
        super(message, errorCode, 400);
    }
}
