package guru.qa.rococo.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class CreateArtistComponent extends BaseComponent<CreateArtistComponent> {

    public CreateArtistComponent() {
        super($("[data-testid='modal-component']"));
    }

    private final SelenideElement name = getSelf().$("input[name='name']");
    private final SelenideElement biography = getSelf().$("textarea[name='biography']");
    private final SelenideElement photo = getSelf().$("input[name='photo']");
    private final SelenideElement addBtn = getSelf().$("button[type='submit']");
    private final SelenideElement closeBtn = getSelf().$("button[type='button']");

    @Step("Установка имени художника: '{name}'")
    public CreateArtistComponent setName(String name) {
        this.name.setValue(name);
        return this;
    }

    @Step("Установка биографии художника: '{biography}'")
    public CreateArtistComponent setBiography(String biography) {
        this.biography.setValue(biography);
        return this;
    }

    @Step("Загрузка фото художника")
    public CreateArtistComponent setPhoto(String photo) {
        this.photo.uploadFromClasspath(photo);
        return this;
    }

    @Step("Нажатие на кнопку 'Добавить'")
    public CreateArtistComponent submitForm() {
        addBtn.click();
        return this;
    }

    @Step("Закрытие формы создания художника")
    public void closeForm() {
        closeBtn.click();
    }

    @Step("Заполнение и отправка формы создания художника")
    public void fillForm(String name, String biography, String photo) {
        setName(name);
        setBiography(biography);
        setPhoto(photo);
        submitForm();
    }

}