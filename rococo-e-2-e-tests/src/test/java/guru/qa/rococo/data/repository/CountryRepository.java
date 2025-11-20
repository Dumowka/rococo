package guru.qa.rococo.data.repository;

import guru.qa.rococo.config.Config;
import guru.qa.rococo.data.entity.museum.CountryEntity;
import jakarta.persistence.EntityManager;

import java.util.UUID;

import static guru.qa.rococo.data.jpa.EntityManagers.em;

public class CountryRepository {

    private static final Config CFG = Config.getInstance();

    private final EntityManager entityManager = em(CFG.museumJdbcUrl());

    public CountryEntity findById(UUID id) {
        return entityManager.find(CountryEntity.class, id);
    }

    public CountryEntity findByName(String name) {
        return entityManager.createQuery("SELECT c FROM CountryEntity c WHERE c.name =: name", CountryEntity.class)
                .setParameter("name", name)
                .getSingleResult();
    }
}
