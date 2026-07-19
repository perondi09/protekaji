package perondi.protekaji.dtos.address;

import jakarta.validation.constraints.NotBlank;

public record AddressDTO(
        @NotBlank String street,
        @NotBlank String street_number,
        String complement,
        @NotBlank String district,
        @NotBlank String city,
        @NotBlank String state,
        @NotBlank String zip_code
) {}