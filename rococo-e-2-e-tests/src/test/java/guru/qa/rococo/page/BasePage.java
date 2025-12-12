package guru.qa.rococo.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.config.Config;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.textsInAnyOrder;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public abstract class BasePage<T extends BasePage<?>> {

    protected static final Config CFG = Config.getInstance();

    protected final SelenideElement alert = $("div[role='alert'] div.MuiAlert-message");
    private final ElementsCollection errors = $$(".form__error");

    public abstract T checkThatPageLoaded();

    @Step("Проверка появления сообщения {text}")
    public T checkAlert(String text) {
        alert.shouldHave(text(text));
        return (T) this;
    }

    @Step("Проверка появления ошибки: {expectedText}")
    public T checkErrorsMessage(String... expectedText) {
        errors.should(textsInAnyOrder(expectedText));
        return (T) this;
    }
}
