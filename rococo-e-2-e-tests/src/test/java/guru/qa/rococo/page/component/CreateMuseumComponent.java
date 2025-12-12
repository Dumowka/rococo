package guru.qa.rococo.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class CreateMuseumComponent extends BaseComponent<CreateMuseumComponent> {

    public CreateMuseumComponent() {
        super($("[data-testid='modal-component']"));
    }

    private final SelenideElement title = getSelf().$("input[name='title']");
    private final SelenideElement country = getSelf().$("select[name='countryId']");
    private final SelenideElement city = getSelf().$("input[name='city']");
    private final SelenideElement photo = getSelf().$("input[name='photo']");
    private final SelenideElement description = getSelf().$("textarea[name='description']");
    private final SelenideElement addBtn = getSelf().$("button[type='submit']");
    private final SelenideElement closeBtn = getSelf().$("button[type='button']");


    @Step("Установка названия музея: '{title}'")
    public CreateMuseumComponent setTitle(String title) {
        this.title.setValue(title);
        return this;
    }

    @Step("Выбор страны: '{country}'")
    public CreateMuseumComponent setCountry(String country) {
        while (!this.country.getOptions().find(text(country)).exists() && this.country.getOptions().size() < 194) {
            this.country.getOptions().last().scrollIntoView(true);
        }
        this.country.selectOption(country);
        return this;
    }

    @Step("Установка города: '{city}'")
    public CreateMuseumComponent setCity(String city) {
        this.city.setValue(city);
        return this;
    }

    @Step("Загрузка фото музея")
    public CreateMuseumComponent setPhoto(String photo) {
        this.photo.uploadFromClasspath(photo);
        return this;
    }

    @Step("Установка описания музея: '{description}'")
    public CreateMuseumComponent setDescription(String description) {
        this.description.setValue(description);
        return this;
    }

    @Step("Нажатие на кнопку 'Добавить'")
    public CreateMuseumComponent submitForm() {
        addBtn.click();
        return this;
    }

    @Step("Закрытие формы создания музея")
    public void closeForm() {
        closeBtn.click();
    }

    @Step("Заполнение и отправка формы создания музея")
    public void fillForm(String title, String country, String city, String photo, String description) {
        setTitle(title);
        setCountry(country);
        setCity(city);
        setPhoto(photo);
        setDescription(description);
        submitForm();
    }
}
