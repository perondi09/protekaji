package perondi.protekaji.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import perondi.protekaji.entities.ExtinguisherEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExtinguisherRepository extends JpaRepository<ExtinguisherEntity, UUID> {

    @Query("SELECT e FROM ExtinguisherEntity e WHERE e.batch_id.id = :batchId")
    List<ExtinguisherEntity> findByBatchId(@Param("batchId") UUID batchId);

    @Query("SELECT e FROM ExtinguisherEntity e WHERE e.serie_number = :serieNumber")
    Optional<ExtinguisherEntity> findBySerieNumber(@Param("serieNumber") String serieNumber);
}