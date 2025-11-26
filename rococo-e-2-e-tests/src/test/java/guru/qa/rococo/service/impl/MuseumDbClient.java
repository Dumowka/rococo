package guru.qa.rococo.service.impl;

import guru.qa.rococo.config.Config;
import guru.qa.rococo.data.entity.museum.MuseumEntity;
import guru.qa.rococo.data.repository.MuseumRepository;
import guru.qa.rococo.data.tpl.XaTransactionTemplate;
import guru.qa.rococo.model.museum.MuseumJson;
import io.qameta.allure.Step;

import java.util.Objects;
import java.util.UUID;

public class MuseumDbClient {

    private static final Config CFG = Config.getInstance();

    private final MuseumRepository museumRepository = new MuseumRepository();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.museumJdbcUrl()
    );

    @Step("Создание музея через БД")
    public MuseumJson createMuseum(MuseumEntity museum) {
        return Objects.requireNonNull(xaTransactionTemplate.execute(() -> MuseumJson.fromEntity(
                museumRepository.create(museum)
        )));
    }

    @Step("Получение музея с идентификатором '{}' через БД")
    public MuseumJson getMuseumById(UUID id) {
        return Objects.requireNonNull(xaTransactionTemplate.execute(() -> MuseumJson.fromEntity(
                museumRepository.findById(id)
        )));
    }
}
