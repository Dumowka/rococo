package guru.qa.rococo.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.config.Config;
import guru.qa.rococo.page.component.CreatePaintingComponent;
import guru.qa.rococo.page.component.ListComponent;
import guru.qa.rococo.page.component.SearchComponent;
import io.qameta.allure.Step;
import lombok.Getter;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class PaintingsPage extends BasePage<PaintingsPage> {

    public static final String URL = Config.getInstance().frontUrl() + "painting";

    private final SelenideElement header = $("h2");
    private final SearchComponent search = new SearchComponent();
    private final SelenideElement addBtn = $("#add-button");

    @Getter
    private final CreatePaintingComponent modalForm = new CreatePaintingComponent();
    private final ListComponent listItems = new ListComponent($("#paintings"));

    @Override
    @Step("Проверка, что страница картин загружена")
    public PaintingsPage checkThatPageLoaded() {
        header.shouldHave(text("Картины"));
        return this;
    }

    @Step("Проверка картины '{title}'")
    public PaintingsPage checkPainting(String title) {
        search.search(title);
        listItems.checkItems(title);
        return this;
    }

    @Step("Нажатие по картине '{title}'")
    public PaintingPage clickToPainting(String title) {
        search.search(title);
        listItems.clickItem(title);
        return new PaintingPage();
    }

    @Step("Проверка количества картин: {expectedSize}")
    public PaintingsPage checkPaintingsSize(int expectedSize) {
        listItems.checkItemsSize(expectedSize);
        return this;
    }

    @Step("Загрузка следующей страницы с картинами")
    public PaintingsPage loadNextPage() {
        listItems.loadNextPage();
        return this;
    }

    @Step("Добавление картины картину")
    public CreatePaintingComponent addPainting() {
        addBtn.shouldBe(visible).click();
        return modalForm;
    }

    @Step("Добавление картины '{title}' с описанием '{description}'")
    public PaintingsPage addPainting(String title, String description, String imgClasspath) {
        addBtn.shouldBe(visible).click();
        modalForm.fillForm(title, description, imgClasspath);
        return this;
    }

    @Step("Добавление картины '{title}' художника '{artistName}' в музей '{museumName}'")
    public PaintingsPage addPainting(String title, String description, String imgClasspath, String artistName, String museumName) {
        addBtn.shouldBe(visible).click();
        modalForm.fillForm(title, description, imgClasspath, artistName, museumName);
        return this;
    }

    @Step("Проверка, что кнопка добавления картины не существует")
    public PaintingsPage checkAddButtonNotExist() {
        addBtn.shouldNot(exist);
        return this;
    }
}
