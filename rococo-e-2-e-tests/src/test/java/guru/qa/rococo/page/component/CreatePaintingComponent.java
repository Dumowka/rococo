package guru.qa.rococo.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class CreatePaintingComponent extends BaseComponent<CreatePaintingComponent> {

    public CreatePaintingComponent() {
        super($("[data-testid='modal-component']"));
    }

    private final SelenideElement title = getSelf().$("input[name='title']");
    private final SelenideElement description = getSelf().$("textarea[name='description']");
    private final SelenideElement content = getSelf().$("input[name='content']");
    private final SelenideElement artist = getSelf().$("select[name='authorId']");
    private final SelenideElement museum = getSelf().$("select[name='museumId']");
    private final SelenideElement addBtn = getSelf().$("button[type='submit']");
    private final SelenideElement closeBtn = getSelf().$("button[type='button']");

    @Step("Установка названия картины: '{title}'")
    public CreatePaintingComponent setTitle(String title) {
        this.title.setValue(title);
        return this;
    }

    @Step("Установка описания картины: '{description}'")
    public CreatePaintingComponent setDescription(String description) {
        this.description.setValue(description);
        return this;
    }

    @Step("Загрузка изображения картины")
    public CreatePaintingComponent setContent(String content) {
        this.content.uploadFromClasspath(content);
        return this;
    }

    @Step("Выбор художника: '{artistName}'")
    public CreatePaintingComponent setArtist(String artistName) {
        while (!this.artist.getOptions().find(text(artistName)).exists()) {
            this.artist.getOptions().last().scrollIntoView(true);
        }
        this.artist.selectOption(artistName);
        return this;
    }

    @Step("Выбор первого художника из списка")
    public CreatePaintingComponent setArtistFirstValue() {
        this.artist.selectOption(0);
        return this;
    }

    @Step("Выбор музея: '{museumName}'")
    public CreatePaintingComponent setMuseum(String museumName) {
        while (!this.museum.getOptions().find(text(museumName)).exists()) {
            this.museum.getOptions().last().scrollIntoView(true);
        }
        this.museum.selectOption(museumName);
        return this;
    }

    @Step("Выбор первого музея из списка")
    public CreatePaintingComponent setMuseumFirstValue() {
        this.museum.selectOption(0);
        return this;
    }

    @Step("Нажатие на кнопку 'Добавить'")
    public CreatePaintingComponent submitForm() {
        addBtn.click();
        return this;
    }

    @Step("Закрытие формы создания картины")
    public void closeForm() {
        closeBtn.click();
    }

    @Step("Заполнение и отправка формы создания картины")
    public void fillForm(String title, String description, String content) {
        setTitle(title);
        setDescription(description);
        setContent(content);
        setArtistFirstValue();
        setMuseumFirstValue();
        submitForm();
    }

    @Step("Заполнение и отправка формы создания картины с выбором художника и музея")
    public void fillForm(String title, String description, String content, String artistName, String museumName) {
        setTitle(title);
        setDescription(description);
        setContent(content);
        if (artistName != null && !artistName.isEmpty()) {
            setArtist(artistName);
        }
        if (museumName != null && !museumName.isEmpty()) {
            setMuseum(museumName);
        }
        submitForm();
    }
}
