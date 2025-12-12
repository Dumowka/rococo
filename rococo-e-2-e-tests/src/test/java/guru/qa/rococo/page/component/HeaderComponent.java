package guru.qa.rococo.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.ArtistPage;
import guru.qa.rococo.page.LoginPage;
import guru.qa.rococo.page.MuseumPage;
import guru.qa.rococo.page.PaintingPage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class HeaderComponent extends BaseComponent<HeaderComponent> {

    private final SelenideElement loginBtn = getSelf().$x(".//button[text()='Войти']");
    private final SelenideElement avatarBtn = getSelf().$("[data-testid='avatar']");

    public HeaderComponent() {
        super($("#shell-header"));
    }

    @Step("Нажатие на кнопку входа в заголовке")
    public LoginPage clickLogin() {
        loginBtn.click();
        return new LoginPage();
    }

    @Step("Нажатие на аватар в заголовке")
    public ProfileComponent clickAvatar() {
        avatarBtn.click();
        return new ProfileComponent();
    }

    @Step("Переход на страницу 'Музеи'")
    public MuseumPage goToMuseums() {
        getSelf().$x("//a[text()='Музеи']").click();
        return new MuseumPage();
    }

    @Step("Переход на страницу 'Художники'")
    public ArtistPage goToArtists() {
        getSelf().$x("//a[text()='Художники']").click();
        return new ArtistPage();
    }

    @Step("Переход на страницу 'Картины'")
    public PaintingPage goToPaintings() {
        getSelf().$x("//a[text()='Картины']").click();
        return new PaintingPage();
    }
}
