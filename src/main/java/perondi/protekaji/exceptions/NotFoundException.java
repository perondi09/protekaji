package perondi.protekaji.exceptions;

public class NotFoundException extends RuntimeException {
    protected NotFoundException(String message) {
        super(message);
    }
}
