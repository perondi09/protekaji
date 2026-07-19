package perondi.protekaji.dtos.company;

import perondi.protekaji.dtos.address.AddressDTO;

import java.util.List;
import java.util.UUID;

public record CompanyResponseDTO(
        UUID id,
        String company_name,
        String cnpj,
        AddressDTO address,
        List<UUID> batches_ids
) {}