package guru.qa.rococo.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.MainPage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class ProfileComponent extends BaseComponent<ProfileComponent> {

    public ProfileComponent() {
        super($("[aria-label='Профиль']"));
    }

    private final SelenideElement logoutBtn = getSelf().$$("button").find(text("Выйти"));
    private final SelenideElement uploadNewPictureButton = $("label[for='image__input']");
    private final SelenideElement photoInput = getSelf().$("input[type='file']");
    private final SelenideElement firstnameInput = getSelf().$("input[name='firstname']");
    private final SelenideElement surnameInput = getSelf().$("input[name='surname']");
    private final SelenideElement updateProfileButton = getSelf().$("button[type='submit']");
    private final SelenideElement closeButton = getSelf().$x("//button[text()='Закрыть']");

    @Step("Выход из системы")
    public MainPage logout() {
        logoutBtn.click();
        return new MainPage();
    }

    @Step("Загрузка фото из classpath")
    public ProfileComponent uploadPhotoFromClasspath(String path) {
        photoInput.uploadFromClasspath(path);
        return this;
    }
}
