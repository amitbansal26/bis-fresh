package in.gov.bis.platform.common.exception;

public class ResourceNotFoundException extends BisException {

    public ResourceNotFoundException(String message) {
        super(message, "RESOURCE_NOT_FOUND", 404);
    }

    public ResourceNotFoundException(String resourceName, Object id) {
        super(resourceName + " not found with id: " + id, "RESOURCE_NOT_FOUND", 404);
    }
}
