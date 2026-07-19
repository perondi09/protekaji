package perondi.protekaji.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import perondi.protekaji.dtos.extinguisher.ExtinguisherDetailedResponseDTO;
import perondi.protekaji.dtos.extinguisher.ExtinguisherPatchDTO;
import perondi.protekaji.dtos.extinguisher.ExtinguisherRequestDTO;
import perondi.protekaji.dtos.extinguisher.ExtinguisherSimpleResponseDTO;
import perondi.protekaji.exceptions.ExtinguisherNotFoundException;
import perondi.protekaji.services.ExtinguisherService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/extinguishers")
@RequiredArgsConstructor
public class ExtinguisherController {

    private final ExtinguisherService extinguisherService;

    @PostMapping
    public ResponseEntity<ExtinguisherSimpleResponseDTO> create(@Valid @RequestBody ExtinguisherRequestDTO extinguisherDto) {
        ExtinguisherSimpleResponseDTO created = extinguisherService.create(extinguisherDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<ExtinguisherSimpleResponseDTO>> findAll(@RequestParam(required = false) UUID batch_id) {
        if (batch_id != null) {
            return ResponseEntity.ok(extinguisherService.findByBatchId(batch_id));
        }
        return ResponseEntity.ok(extinguisherService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExtinguisherDetailedResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(extinguisherService.findById(id));
    }

    @GetMapping("/serie/{serieNumber}")
    public ResponseEntity<ExtinguisherDetailedResponseDTO> findBySerieNumber(@PathVariable String serieNumber) {
        return extinguisherService.findBySerieNumber(serieNumber)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ExtinguisherNotFoundException(serieNumber));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ExtinguisherSimpleResponseDTO> update(@PathVariable UUID id,
                                                                @RequestBody ExtinguisherPatchDTO extinguisherDto) {
        return ResponseEntity.ok(extinguisherService.update(id, extinguisherDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        extinguisherService.delete(id);
        return ResponseEntity.noContent().build();
    }
}