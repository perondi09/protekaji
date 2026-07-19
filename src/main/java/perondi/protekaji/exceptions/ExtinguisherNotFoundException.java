package perondi.protekaji.exceptions;

import java.util.UUID;

public class ExtinguisherNotFoundException extends NotFoundException {

    public ExtinguisherNotFoundException(UUID id) {
        super("Extintor não encontrado com o id: " + id);
    }

    public ExtinguisherNotFoundException(String serieNumber) {
        super("Extintor não encontrado com o número de série: " + serieNumber);
    }
}
