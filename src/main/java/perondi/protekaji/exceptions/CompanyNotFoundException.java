package perondi.protekaji.exceptions;

import java.util.UUID;

public class CompanyNotFoundException extends NotFoundException {
    public CompanyNotFoundException(UUID id) {
        super("Empresa não encontrada com o id: " + id);
    }
}
