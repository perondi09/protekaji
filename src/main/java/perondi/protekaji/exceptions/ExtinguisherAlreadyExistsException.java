package perondi.protekaji.exceptions;

public class ExtinguisherAlreadyExistsException extends ConflictException {
    public ExtinguisherAlreadyExistsException(String serieNumber) {
        super("Já existe um extintor cadastrado com o número de série: " + serieNumber);
    }
}
