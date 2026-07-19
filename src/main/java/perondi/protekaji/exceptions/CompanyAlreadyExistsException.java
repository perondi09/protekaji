package perondi.protekaji.exceptions;

public class CompanyAlreadyExistsException extends ConflictException {
    public CompanyAlreadyExistsException(String cnpj) {
        super("Já existe uma empresa cadastrada com o CNPJ: " + cnpj);
    }
}
