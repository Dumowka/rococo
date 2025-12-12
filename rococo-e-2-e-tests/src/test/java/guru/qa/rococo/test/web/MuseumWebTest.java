package guru.qa.rococo.test.web;


import com.codeborne.selenide.Selenide;
import guru.qa.rococo.jupiter.annotation.ApiLogin;
import guru.qa.rococo.jupiter.annotation.Museum;
import guru.qa.rococo.jupiter.annotation.Museums;
import guru.qa.rococo.jupiter.annotation.User;
import guru.qa.rococo.jupiter.annotation.WebTest;
import guru.qa.rococo.model.Countries;
import guru.qa.rococo.model.PhotoPaths;
import guru.qa.rococo.model.museum.MuseumJson;
import guru.qa.rococo.page.MainPage;
import guru.qa.rococo.page.MuseumPage;
import guru.qa.rococo.page.MuseumsPage;
import guru.qa.rococo.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

@WebTest
public class MuseumWebTest {

    @Test
    @ApiLogin
    @User
    void shouldAddMuseumByAuthorizedUser() {
        String museumName = RandomDataUtils.randomMuseumName();

        Selenide.open(MainPage.URL, MainPage.class)
                .goToMuseums()
                .addMuseum(museumName,
                        Countries.ARMENIA.getName(),
                        RandomDataUtils.randomCityName(),
                        PhotoPaths.MUSEUM,
                        RandomDataUtils.randomSentence(20)
                )
                .checkMuseum(museumName);
    }

    @Test
    @User
    @ApiLogin
    @Museum
    void shouldUpdateMuseumByAuthorizedUser(MuseumJson museum) {
        String randomCityName = RandomDataUtils.randomCityName();
        String randomDescription = RandomDataUtils.randomSentence(20);
        Selenide.open(MuseumsPage.URL, MuseumsPage.class)
                .clickToMuseum(museum.title())
                .editMuseum()
                .setDescription(randomDescription)
                .setCity(randomCityName)
                .submitForm();
        new MuseumPage()
                .checkCountryCity(randomCityName)
                .checkDescription(randomDescription);
    }

    @Test
    @Museums(count = 4)
    void shouldLoadMuseumPageForUnauthorizedUser() {
        Selenide.open(MuseumsPage.URL, MuseumsPage.class)
                .checkMuseumsSize(4);
    }

    @Test
    @User
    @ApiLogin
    @Museum
    void shouldFindMuseumByTitle(MuseumJson museum) {
        Selenide.open(MuseumsPage.URL, MuseumsPage.class)
                .checkMuseum(museum.title())
                .checkMuseumsSize(1);
    }

    @Test
    @User
    @ApiLogin
    @Museums(count = 8)
    void shouldLoadMuseumByPageByAuthorizedUser() {
        Selenide.open(MuseumsPage.URL, MuseumsPage.class)
                .checkMuseumsSize(4)
                .loadNextPage()
                .checkMuseumsSize(8);
    }

    @Test
    @Museums(count = 8)
    void shouldLoadMuseumByPageByUnauthorizedUser() {
        Selenide.open(MuseumsPage.URL, MuseumsPage.class)
                .checkMuseumsSize(4)
                .loadNextPage()
                .checkMuseumsSize(8);
    }
}
