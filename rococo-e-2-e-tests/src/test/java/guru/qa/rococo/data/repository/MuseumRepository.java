package guru.qa.rococo.data.repository;

import guru.qa.rococo.config.Config;
import guru.qa.rococo.data.entity.museum.MuseumEntity;
import jakarta.persistence.EntityManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

import static guru.qa.rococo.data.jpa.EntityManagers.em;

public class MuseumRepository {

    private static final Config CFG = Config.getInstance();

    private final EntityManager entityManager = em(CFG.museumJdbcUrl());

    @Nonnull
    public MuseumEntity create(MuseumEntity museum) {
        entityManager.joinTransaction();
        entityManager.persist(museum);
        return museum;
    }

    @Nullable
    public MuseumEntity findById(UUID id) {
        return entityManager.find(MuseumEntity.class, id);
    }
}
