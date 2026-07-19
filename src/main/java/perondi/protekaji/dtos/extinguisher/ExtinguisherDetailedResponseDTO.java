package perondi.protekaji.dtos.extinguisher;

import perondi.protekaji.entities.enums.ExtinguisherType;
import perondi.protekaji.entities.enums.Unit;

import java.util.UUID;

public record ExtinguisherDetailedResponseDTO(
        UUID company_id,
        String company_cnpj,
        UUID batch_id,
        UUID id,
        String serie_number,
        ExtinguisherType extinguisher_type,
        Unit unit,
        String manufacturer
) {}
