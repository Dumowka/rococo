package guru.qa.rococo.service.impl;

import guru.qa.rococo.config.Config;
import guru.qa.rococo.data.entity.artist.ArtistEntity;
import guru.qa.rococo.data.repository.ArtistRepository;
import guru.qa.rococo.data.tpl.XaTransactionTemplate;
import guru.qa.rococo.model.artist.ArtistJson;
import io.qameta.allure.Step;

import java.util.Objects;
import java.util.UUID;

public class ArtistDbClient {

    private static final Config CFG = Config.getInstance();

    private final ArtistRepository artistRepository = new ArtistRepository();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.artistJdbcUrl()
    );

    @Step("Создание художника через БД")
    public ArtistJson createArtist(ArtistEntity artist) {
        return Objects.requireNonNull(xaTransactionTemplate.execute(() -> ArtistJson.fromEntity(
                artistRepository.create(artist)
        )));
    }

    @Step("Получение художника с идентификатором '{}' через БД")
    public ArtistJson getArtistById(UUID id) {
        return Objects.requireNonNull(xaTransactionTemplate.execute(() -> ArtistJson.fromEntity(
                artistRepository.findById(id)
        )));
    }
}
