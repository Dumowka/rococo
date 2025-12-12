package guru.qa.rococo.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class SearchComponent extends BaseComponent<SearchComponent> {

    public SearchComponent(SelenideElement self) {
        super(self);
    }

    public SearchComponent() {
        super($("[type='search']").parent());
    }

    private final SelenideElement input = getSelf().$("input[type='search']");
    private final SelenideElement searchBtn = getSelf().$("button");


    @Step("Поиск по тексту '{text}'")
    public SearchComponent search(String text) {
        input.setValue(text).pressEnter();
        return this;
    }
}
