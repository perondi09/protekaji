package perondi.protekaji.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perondi.protekaji.dtos.batch.BatchRequestDTO;
import perondi.protekaji.dtos.batch.BatchResponseDTO;
import perondi.protekaji.dtos.extinguisher.ExtinguisherSimpleResponseDTO;
import perondi.protekaji.entities.BatchEntity;
import perondi.protekaji.entities.CompanyEntity;
import perondi.protekaji.exceptions.BatchNotFoundException;
import perondi.protekaji.exceptions.CompanyNotFoundException;
import perondi.protekaji.repositories.BatchRepository;
import perondi.protekaji.repositories.CompanyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BatchService {

    private final BatchRepository batchRepository;
    private final CompanyRepository companyRepository;

    @Transactional
    public BatchResponseDTO create(BatchRequestDTO dto) {
        CompanyEntity company = companyRepository.findById(dto.company_id())
                .orElseThrow(() -> new CompanyNotFoundException(dto.company_id()));

        BatchEntity batch = new BatchEntity();
        batch.setCompany_id(company);

        BatchEntity saved = batchRepository.save(batch);
        log.info("Lote {} criado para a empresa {}", saved.getId(), company.getId());

        return toResponseDTO(saved);
    }

    public List<BatchResponseDTO> findAll() {
        log.debug("Listando todos os lotes");
        return batchRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<BatchResponseDTO> findByCompanyId(UUID company_id) {
        log.debug("Listando lotes da empresa {}", company_id);
        return batchRepository.findByCompanyId(company_id).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public BatchResponseDTO findById(UUID id) {
        log.debug("Buscando lote {}", id);
        BatchEntity batch = batchRepository.findById(id)
                .orElseThrow(() -> new BatchNotFoundException(id));
        return toResponseDTO(batch);
    }

    @Transactional
    public void delete(UUID id) {
        BatchEntity existing = batchRepository.findById(id)
                .orElseThrow(() -> new BatchNotFoundException(id));
        batchRepository.delete(existing);
        log.info("Lote {} removido", id);
    }

    private BatchResponseDTO toResponseDTO(BatchEntity entity) {
        List<ExtinguisherSimpleResponseDTO> extinguishers = new ArrayList<>();

        if (entity.getExtinguisher() != null) {
            extinguishers = entity.getExtinguisher().stream()
                    .map(ext -> new ExtinguisherSimpleResponseDTO(
                            ext.getId(),
                            ext.getSerie_number(),
                            ext.getExtinguisher_type(),
                            ext.getUnit(),
                            ext.getManufacturer()
                    )).collect(Collectors.toList());
        }

        return new BatchResponseDTO(
                entity.getCompany_id().getId(),
                entity.getId(),
                extinguishers
        );
    }
}