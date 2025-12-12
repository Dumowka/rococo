package guru.qa.rococo.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.component.CreateMuseumComponent;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MuseumPage extends BasePage<MuseumPage> {

    private final SelenideElement editBtn = $("[data-testid=edit-museum]");
    private final SelenideElement description = $("#description");
    private final SelenideElement countryCity = $("div.text-center");

    @Override
    public MuseumPage checkThatPageLoaded() {
        editBtn.shouldBe(visible);
        description.shouldBe(visible);
        countryCity.shouldBe(visible);
        return this;
    }

    @Step("Открытие редактирования музея")
    public CreateMuseumComponent editMuseum() {
        editBtn.click();
        return new CreateMuseumComponent();
    }

    @Step("Проверка описания '{expectedText}'")
    public MuseumPage checkDescription(String expectedText) {
        description.shouldHave(text(expectedText));
        return this;
    }

    @Step("Проверка страны и города '{expectedText}'")
    public MuseumPage checkCountryCity(String expectedText) {
        countryCity.shouldHave(text(expectedText));
        return this;
    }
}
