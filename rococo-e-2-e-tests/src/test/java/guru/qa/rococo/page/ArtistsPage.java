package guru.qa.rococo.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.config.Config;
import guru.qa.rococo.page.component.CreateArtistComponent;
import guru.qa.rococo.page.component.ListComponent;
import guru.qa.rococo.page.component.SearchComponent;
import io.qameta.allure.Step;
import lombok.Getter;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class ArtistsPage extends BasePage<ArtistsPage> {

    public static final String URL = Config.getInstance().frontUrl() + "artist";

    private final SelenideElement header = $("h2");
    private final SearchComponent search = new SearchComponent();
    private final SelenideElement addBtn = $("#add-button");

    @Getter
    private final CreateArtistComponent modalForm = new CreateArtistComponent();
    private final ListComponent listItems = new ListComponent($("#artists"));

    @Override
    @Step("Проверка, что страница художников загружена")
    public ArtistsPage checkThatPageLoaded() {
        header.shouldHave(text("Художники"));
        return this;
    }

    @Step("Добавление художника")
    public CreateArtistComponent addArtist() {
        addBtn.shouldBe(visible).click();
        return modalForm;
    }

    @Step("Проверка художника '{name}'")
    public ArtistsPage checkArtist(String name) {
        search.search(name);
        listItems.checkItems(name);
        return this;
    }

    @Step("Нажатие по художнику '{name}'")
    public ArtistPage clickToArtist(String name) {
        search.search(name);
        listItems.clickItem(name);
        return new ArtistPage();
    }

    @Step("Проверка количества художников: {expectedSize}")
    public ArtistsPage checkArtistsSize(int expectedSize) {
        listItems.checkItemsSize(expectedSize);
        return this;
    }

    @Step("Проверка, что количество художников больше или равно: {expectedSize}")
    public ArtistsPage checkArtistsGreaterOrEqThan(int expectedSize) {
        listItems.checkItemsSizeMore(expectedSize);
        return this;
    }

    @Step("Загрузка следующей страницы с художниками")
    public ArtistsPage loadNextPage() {
        listItems.loadNextPage();
        return this;
    }

    @Step("Добавление художника '{artistName}' с биографией '{biography}'")
    public ArtistsPage addArtist(String artistName, String biography, String imgClasspath) {
        addBtn.shouldBe(visible).click();
        modalForm.fillForm(artistName, biography, imgClasspath);
        return this;
    }

    @Step("Проверка, что кнопки добавления художника не существует")
    public ArtistsPage checkAddButtonNotExist() {
        addBtn.shouldNot(exist);
        return this;
    }
}
