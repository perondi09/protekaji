    package perondi.protekaji.dtos.company;

    import jakarta.validation.Valid;
    import jakarta.validation.constraints.NotBlank;
    import perondi.protekaji.dtos.address.AddressDTO;

    public record CompanyRequestDTO(
            @NotBlank String company_name,
            @NotBlank String cnpj,
            @Valid AddressDTO address
    ) {}