package guru.qa.rococo.service.impl;

import guru.qa.rococo.config.Config;
import guru.qa.rococo.data.entity.painting.PaintingEntity;
import guru.qa.rococo.data.repository.PaintingRepository;
import guru.qa.rococo.data.tpl.XaTransactionTemplate;
import guru.qa.rococo.model.painting.PaintingJson;
import io.qameta.allure.Step;

import java.util.Objects;

public class PaintingDbClient {

    private static final Config CFG = Config.getInstance();

    private final PaintingRepository paintingRepository = new PaintingRepository();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.paintingJdbcUrl()
    );

    @Step("Создание картины через БД")
    public PaintingJson createPainting(PaintingEntity painting) {
        return Objects.requireNonNull(xaTransactionTemplate.execute(() -> PaintingJson.fromEntity(
                paintingRepository.create(painting)
        )));
    }
}
