package perondi.protekaji.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import perondi.protekaji.entities.CompanyEntity;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<CompanyEntity, UUID> {

    Optional<CompanyEntity> findByCnpj(String cnpj);

}