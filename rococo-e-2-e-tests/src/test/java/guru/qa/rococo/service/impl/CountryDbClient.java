package guru.qa.rococo.service.impl;

import guru.qa.rococo.config.Config;
import guru.qa.rococo.data.entity.museum.CountryEntity;
import guru.qa.rococo.data.repository.CountryRepository;
import guru.qa.rococo.data.tpl.XaTransactionTemplate;
import io.qameta.allure.Step;

import java.util.Objects;

public class CountryDbClient {

    private static final Config CFG = Config.getInstance();

    private final CountryRepository countryRepository = new CountryRepository();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.museumJdbcUrl()
    );

    @Step("Получение страны '{0}' через БД")
    public CountryEntity findByName(String name) {
        return Objects.requireNonNull(xaTransactionTemplate.execute(() ->
                countryRepository.findByName(name)
        ));
    }
}
