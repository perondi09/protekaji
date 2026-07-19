package perondi.protekaji.services;

import lombok.RequiredArgsConstructor;
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
        return toResponseDTO(saved);
    }

    public List<BatchResponseDTO> findAll() {
        return batchRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<BatchResponseDTO> findByCompanyId(UUID company_id) {
        return batchRepository.findByCompanyId(company_id).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public BatchResponseDTO findById(UUID id) {
        BatchEntity batch = batchRepository.findById(id)
                .orElseThrow(() -> new BatchNotFoundException(id));
        return toResponseDTO(batch);
    }

    @Transactional
    public void delete(UUID id) {
        BatchEntity existing = batchRepository.findById(id)
                .orElseThrow(() -> new BatchNotFoundException(id));
        batchRepository.delete(existing);
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