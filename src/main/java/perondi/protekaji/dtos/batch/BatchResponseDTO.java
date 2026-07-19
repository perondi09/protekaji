package perondi.protekaji.dtos.batch;

import perondi.protekaji.dtos.extinguisher.ExtinguisherSimpleResponseDTO;

import java.util.List;
import java.util.UUID;

public record BatchResponseDTO(
        UUID company_id,
        UUID batch_id,
        List<ExtinguisherSimpleResponseDTO> extinguishers
) {}