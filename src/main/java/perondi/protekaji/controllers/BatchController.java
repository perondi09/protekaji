package perondi.protekaji.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import perondi.protekaji.dtos.batch.BatchRequestDTO;
import perondi.protekaji.dtos.batch.BatchResponseDTO;
import perondi.protekaji.services.BatchService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/batches")
@RequiredArgsConstructor
public class BatchController {

    private final BatchService batchService;

    @PostMapping
    public ResponseEntity<BatchResponseDTO> create(@Valid @RequestBody BatchRequestDTO batchDto) {
        BatchResponseDTO created = batchService.create(batchDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<BatchResponseDTO>> findAll(@RequestParam(required = false) UUID company_id) {
        if (company_id != null) {
            return ResponseEntity.ok(batchService.findByCompanyId(company_id));
        }
        return ResponseEntity.ok(batchService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BatchResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(batchService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        batchService.delete(id);
        return ResponseEntity.noContent().build();
    }
}