package guru.qa.rococo.data.repository;

import guru.qa.rococo.config.Config;
import guru.qa.rococo.data.entity.artist.ArtistEntity;
import jakarta.persistence.EntityManager;

import javax.annotation.Nonnull;

import static guru.qa.rococo.data.jpa.EntityManagers.em;

public class ArtistRepository {

    private static final Config CFG = Config.getInstance();

    private final EntityManager entityManager = em(CFG.artistJdbcUrl());

    @Nonnull
    public ArtistEntity create(ArtistEntity artist) {
        entityManager.joinTransaction();
        entityManager.persist(artist);
        return artist;
    }
}
