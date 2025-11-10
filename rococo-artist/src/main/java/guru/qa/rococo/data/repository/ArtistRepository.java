package guru.qa.rococo.data.repository;

import guru.qa.rococo.data.ArtistEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ArtistRepository extends JpaRepository<ArtistEntity, UUID> {
    Optional<ArtistEntity> findById(UUID id);

    Page<ArtistEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
