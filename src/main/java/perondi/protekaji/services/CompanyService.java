package perondi.protekaji.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perondi.protekaji.dtos.address.AddressDTO;
import perondi.protekaji.dtos.company.CompanyPatchDTO;
import perondi.protekaji.dtos.company.CompanyRequestDTO;
import perondi.protekaji.dtos.company.CompanyResponseDTO;
import perondi.protekaji.entities.AddressEntity;
import perondi.protekaji.entities.BatchEntity;
import perondi.protekaji.entities.CompanyEntity;
import perondi.protekaji.exceptions.CompanyAlreadyExistsException;
import perondi.protekaji.exceptions.CompanyNotFoundException;
import perondi.protekaji.repositories.BatchRepository;
import perondi.protekaji.repositories.CompanyRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final BatchRepository batchRepository;

    @Transactional
    public CompanyResponseDTO create(CompanyRequestDTO dto) {
        if (companyRepository.findByCnpj(dto.cnpj()).isPresent()) {
            throw new CompanyAlreadyExistsException(dto.cnpj());
        }

        CompanyEntity company = new CompanyEntity();
        company.setCompany_name(dto.company_name());
        company.setCnpj(dto.cnpj());
        company.setAddressEntity(toAddressEntity(dto.address()));

        CompanyEntity saved = companyRepository.save(company);
        log.info("Empresa {} criada (CNPJ {})", saved.getId(), saved.getCnpj());

        return toResponseDTO(saved, List.of());
    }

    public List<CompanyResponseDTO> findAll() {
        log.debug("Listando todas as empresas");

        List<CompanyEntity> companies = companyRepository.findAll();
        List<UUID> companyIds = companies.stream().map(CompanyEntity::getId).collect(Collectors.toList());

        Map<UUID, List<UUID>> batchIdsByCompany = batchRepository.findByCompanyIdIn(companyIds).stream()
                .collect(Collectors.groupingBy(
                        b -> b.getCompany_id().getId(),
                        Collectors.mapping(BatchEntity::getId, Collectors.toList())
                ));

        return companies.stream()
                .map(company -> toResponseDTO(company, batchIdsByCompany.getOrDefault(company.getId(), List.of())))
                .collect(Collectors.toList());
    }

    public CompanyResponseDTO findById(UUID id) {
        log.debug("Buscando empresa {}", id);

        CompanyEntity company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException(id));

        List<UUID> batchIds = batchRepository.findByCompanyId(id).stream()
                .map(BatchEntity::getId)
                .collect(Collectors.toList());

        return toResponseDTO(company, batchIds);
    }

    @Transactional
    public CompanyResponseDTO update(UUID id, CompanyPatchDTO dto) {
        CompanyEntity existing = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException(id));

        if (dto.company_name() != null) {
            existing.setCompany_name(dto.company_name());
        }

        if (dto.cnpj() != null && !dto.cnpj().equals(existing.getCnpj())) {
            if (companyRepository.findByCnpj(dto.cnpj()).isPresent()) {
                throw new CompanyAlreadyExistsException(dto.cnpj());
            }
            existing.setCnpj(dto.cnpj());
        }

        if (dto.address() != null) {
            existing.setAddressEntity(toAddressEntity(dto.address()));
        }

        CompanyEntity saved = companyRepository.save(existing);
        log.info("Empresa {} atualizada", saved.getId());

        List<UUID> batchIds = batchRepository.findByCompanyId(id).stream()
                .map(BatchEntity::getId)
                .collect(Collectors.toList());

        return toResponseDTO(saved, batchIds);
    }

    @Transactional
    public void delete(UUID id) {
        CompanyEntity existing = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException(id));
        companyRepository.delete(existing);
        log.info("Empresa {} removida", id);
    }

    private CompanyResponseDTO toResponseDTO(CompanyEntity entity, List<UUID> batchesIds) {
        AddressDTO addressDTO = new AddressDTO(
                entity.getAddressEntity().getStreet(),
                entity.getAddressEntity().getStreet_number(),
                entity.getAddressEntity().getComplement(),
                entity.getAddressEntity().getDistrict(),
                entity.getAddressEntity().getCity(),
                entity.getAddressEntity().getState(),
                entity.getAddressEntity().getZip_code()
        );

        return new CompanyResponseDTO(
                entity.getId(),
                entity.getCompany_name(),
                entity.getCnpj(),
                addressDTO,
                batchesIds
        );
    }

    private AddressEntity toAddressEntity(AddressDTO dto) {
        return new AddressEntity(
                dto.street(),
                dto.street_number(),
                dto.complement(),
                dto.district(),
                dto.city(),
                dto.state(),
                dto.zip_code()
        );
    }
}