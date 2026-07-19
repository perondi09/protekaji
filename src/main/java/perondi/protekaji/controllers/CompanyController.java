package perondi.protekaji.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import perondi.protekaji.dtos.company.CompanyPatchDTO;
import perondi.protekaji.dtos.company.CompanyRequestDTO;
import perondi.protekaji.dtos.company.CompanyResponseDTO;
import perondi.protekaji.services.CompanyService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    public ResponseEntity<CompanyResponseDTO> create(@Valid @RequestBody CompanyRequestDTO companyDto) {
        CompanyResponseDTO created = companyService.create(companyDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<CompanyResponseDTO>> findAll() {
        return ResponseEntity.ok(companyService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(companyService.findById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CompanyResponseDTO> update(@PathVariable UUID id, @RequestBody CompanyPatchDTO companyDto) {
        return ResponseEntity.ok(companyService.update(id, companyDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        companyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}