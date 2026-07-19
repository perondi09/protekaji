package perondi.protekaji.dtos.company;

import perondi.protekaji.dtos.address.AddressDTO;

public record CompanyPatchDTO(
        String company_name,
        String cnpj,
        AddressDTO address
) {}    