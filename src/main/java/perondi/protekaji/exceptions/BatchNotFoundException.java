package perondi.protekaji.exceptions;

import java.util.UUID;

public class BatchNotFoundException extends NotFoundException {
    public BatchNotFoundException(UUID id) {
        super("Lote não encontrado com o id: " + id);
    }
}
