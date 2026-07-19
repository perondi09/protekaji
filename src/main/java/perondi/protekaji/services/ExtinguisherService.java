package perondi.protekaji.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perondi.protekaji.dtos.extinguisher.ExtinguisherDetailedResponseDTO;
import perondi.protekaji.dtos.extinguisher.ExtinguisherPatchDTO;
import perondi.protekaji.dtos.extinguisher.ExtinguisherRequestDTO;
import perondi.protekaji.dtos.extinguisher.ExtinguisherSimpleResponseDTO;
import perondi.protekaji.entities.BatchEntity;
import perondi.protekaji.entities.ExtinguisherEntity;
import perondi.protekaji.exceptions.BatchNotFoundException;
import perondi.protekaji.exceptions.ExtinguisherAlreadyExistsException;
import perondi.protekaji.exceptions.ExtinguisherNotFoundException;
import perondi.protekaji.repositories.BatchRepository;
import perondi.protekaji.repositories.ExtinguisherRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExtinguisherService {

    private final ExtinguisherRepository extinguisherRepository;
    private final BatchRepository batchRepository;

    @Transactional
    public ExtinguisherSimpleResponseDTO create(ExtinguisherRequestDTO dto) {
        if (extinguisherRepository.findBySerieNumber(dto.serie_number()).isPresent()) {
            throw new ExtinguisherAlreadyExistsException(dto.serie_number());
        }

        BatchEntity batch = batchRepository.findById(dto.batch_id())
                .orElseThrow(() -> new BatchNotFoundException(dto.batch_id()));

        ExtinguisherEntity extinguisher = new ExtinguisherEntity();
        extinguisher.setBatch_id(batch);
        extinguisher.setSerie_number(dto.serie_number());
        extinguisher.setExtinguisher_type(dto.extinguisher_type());
        extinguisher.setUnit(dto.unit());
        extinguisher.setManufacturer(dto.manufacturer());

        ExtinguisherEntity saved = extinguisherRepository.save(extinguisher);
        log.info("Extintor {} (série {}) criado no lote {}", saved.getId(), saved.getSerie_number(), batch.getId());

        return toSimpleResponseDTO(saved);
    }

    public List<ExtinguisherSimpleResponseDTO> findAll() {
        log.debug("Listando todos os extintores");
        return extinguisherRepository.findAll().stream()
                .map(this::toSimpleResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ExtinguisherSimpleResponseDTO> findByBatchId(UUID batch_id) {
        log.debug("Listando extintores do lote {}", batch_id);
        return extinguisherRepository.findByBatchId(batch_id).stream()
                .map(this::toSimpleResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<ExtinguisherDetailedResponseDTO> findBySerieNumber(String serieNumber) {
        log.debug("Buscando extintor pela série {}", serieNumber);
        return extinguisherRepository.findBySerieNumber(serieNumber)
                .map(this::toDetailedResponseDTO);
    }

    public ExtinguisherDetailedResponseDTO findById(UUID id) {
        log.debug("Buscando extintor {}", id);
        ExtinguisherEntity extinguisher = extinguisherRepository.findById(id)
                .orElseThrow(() -> new ExtinguisherNotFoundException(id));
        return toDetailedResponseDTO(extinguisher);
    }

    @Transactional
    public ExtinguisherSimpleResponseDTO update(UUID id, ExtinguisherPatchDTO dto) {
        ExtinguisherEntity existing = extinguisherRepository.findById(id)
                .orElseThrow(() -> new ExtinguisherNotFoundException(id));

        if (dto.serie_number() != null && !dto.serie_number().equals(existing.getSerie_number())) {
            if (extinguisherRepository.findBySerieNumber(dto.serie_number()).isPresent()) {
                throw new ExtinguisherAlreadyExistsException(dto.serie_number());
            }
            existing.setSerie_number(dto.serie_number());
        }

        if (dto.extinguisher_type() != null) {
            existing.setExtinguisher_type(dto.extinguisher_type());
        }

        if (dto.unit() != null) {
            existing.setUnit(dto.unit());
        }

        if (dto.manufacturer() != null) {
            existing.setManufacturer(dto.manufacturer());
        }

        if (dto.batch_id() != null) {
            BatchEntity batch = batchRepository.findById(dto.batch_id())
                    .orElseThrow(() -> new BatchNotFoundException(dto.batch_id()));
            existing.setBatch_id(batch);
        }

        ExtinguisherEntity saved = extinguisherRepository.save(existing);
        log.info("Extintor {} atualizado", saved.getId());

        return toSimpleResponseDTO(saved);
    }

    @Transactional
    public void delete(UUID id) {
        ExtinguisherEntity existing = extinguisherRepository.findById(id)
                .orElseThrow(() -> new ExtinguisherNotFoundException(id));
        extinguisherRepository.delete(existing);
        log.info("Extintor {} removido", id);
    }

    private ExtinguisherSimpleResponseDTO toSimpleResponseDTO(ExtinguisherEntity entity) {
        return new ExtinguisherSimpleResponseDTO(
                entity.getId(),
                entity.getSerie_number(),
                entity.getExtinguisher_type(),
                entity.getUnit(),
                entity.getManufacturer()
        );
    }

    private ExtinguisherDetailedResponseDTO toDetailedResponseDTO(ExtinguisherEntity entity) {
        return new ExtinguisherDetailedResponseDTO(
                entity.getBatch_id().getCompany_id().getId(),
                entity.getBatch_id().getCompany_id().getCnpj(),
                entity.getBatch_id().getId(),
                entity.getId(),
                entity.getSerie_number(),
                entity.getExtinguisher_type(),
                entity.getUnit(),
                entity.getManufacturer()
        );
    }
}