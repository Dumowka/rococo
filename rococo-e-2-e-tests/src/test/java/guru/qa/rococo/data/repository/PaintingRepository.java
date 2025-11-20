package guru.qa.rococo.data.repository;

import guru.qa.rococo.config.Config;
import guru.qa.rococo.data.entity.painting.PaintingEntity;
import jakarta.persistence.EntityManager;

import javax.annotation.Nonnull;

import static guru.qa.rococo.data.jpa.EntityManagers.em;

public class PaintingRepository {

    private static final Config CFG = Config.getInstance();

    private final EntityManager entityManager = em(CFG.paintingJdbcUrl());

    @Nonnull
    public PaintingEntity create(PaintingEntity painting) {
        entityManager.joinTransaction();
        entityManager.persist(painting);
        return painting;
    }
}
