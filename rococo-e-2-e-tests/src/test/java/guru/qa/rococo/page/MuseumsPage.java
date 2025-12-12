package guru.qa.rococo.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.config.Config;
import guru.qa.rococo.page.component.CreateMuseumComponent;
import guru.qa.rococo.page.component.ListComponent;
import guru.qa.rococo.page.component.SearchComponent;
import io.qameta.allure.Step;
import lombok.Getter;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MuseumsPage extends BasePage<MuseumsPage> {

    public static final String URL =  Config.getInstance().frontUrl() + "museum";

    private final SelenideElement header = $("h2");
    private final SelenideElement addButton = $("#add-button");

    @Getter
    private final CreateMuseumComponent modalForm = new CreateMuseumComponent();
    private final SearchComponent search = new SearchComponent();
    private final ListComponent listItems = new ListComponent($("#museums"));

    @Override
    @Step("Проверка, что страница музеев загружена")
    public MuseumsPage checkThatPageLoaded() {
        header.shouldHave(text("Музеи"));
        return this;
    }

    @Step("Проверка наличия музея '{title}'")
    public MuseumsPage checkMuseum(String title) {
        search.search(title);
        listItems.checkItems(title);
        return this;
    }

    @Step("Нажатие на музей '{title}'")
    public MuseumPage clickToMuseum(String title) {
        search.search(title);
        listItems.clickItem(title);
        return new MuseumPage();
    }

    @Step("Проверка количества музеев: {expectedSize}")
    public MuseumsPage checkMuseumsSize(int expectedSize) {
        listItems.checkItemsSize(expectedSize);
        return this;
    }

    @Step("Загрузка следующей страницы с музеями")
    public MuseumsPage loadNextPage() {
        listItems.loadNextPage();
        return this;
    }

    @Step("Добавление музея '{museumTitle}' в стране '{country}', городе '{city}'")
    public MuseumsPage addMuseum(String museumTitle, String country, String city, String imgClasspath, String description) {
        addButton.shouldBe(visible).click();
        modalForm.fillForm(museumTitle, country, city, imgClasspath, description);
        return this;
    }

    @Step("Проверка, что кнопки добавления музея не существует")
    public MuseumsPage checkAddButtonNotExist() {
        header.shouldNot(exist);
        return this;
    }
}
