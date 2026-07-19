package perondi.protekaji.dtos.extinguisher;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import perondi.protekaji.entities.enums.ExtinguisherType;
import perondi.protekaji.entities.enums.Unit;

import java.util.UUID;

public record ExtinguisherRequestDTO(
        @NotNull UUID batch_id,
        @NotBlank String serie_number,
        @NotNull ExtinguisherType extinguisher_type,
        @NotNull Unit unit,
        @NotBlank String manufacturer
) {}