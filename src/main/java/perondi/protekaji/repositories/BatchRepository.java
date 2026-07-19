package perondi.protekaji.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import perondi.protekaji.entities.BatchEntity;

import java.util.List;
import java.util.UUID;

public interface BatchRepository extends JpaRepository<BatchEntity, UUID> {

    @Query("SELECT b FROM BatchEntity b WHERE b.company_id.id = :companyId")
    List<BatchEntity> findByCompanyId(@Param("companyId") UUID companyId);

    @Query("SELECT b FROM BatchEntity b WHERE b.company_id.id IN :companyIds")
    List<BatchEntity> findByCompanyIdIn(@Param("companyIds") List<UUID> companyIds);
}