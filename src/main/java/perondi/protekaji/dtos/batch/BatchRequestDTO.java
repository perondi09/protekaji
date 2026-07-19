package perondi.protekaji.dtos.batch;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record BatchRequestDTO(
        @NotNull UUID company_id
) {}