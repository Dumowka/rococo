package guru.qa.rococo.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.rococo.jupiter.annotation.ApiLogin;
import guru.qa.rococo.jupiter.annotation.Artist;
import guru.qa.rococo.jupiter.annotation.Artists;
import guru.qa.rococo.jupiter.annotation.User;
import guru.qa.rococo.jupiter.annotation.WebTest;
import guru.qa.rococo.model.PhotoPaths;
import guru.qa.rococo.model.artist.ArtistJson;
import guru.qa.rococo.page.ArtistPage;
import guru.qa.rococo.page.ArtistsPage;
import guru.qa.rococo.page.MainPage;
import guru.qa.rococo.utils.RandomDataUtils;
import guru.qa.rococo.utils.ResourceUtils;
import org.junit.jupiter.api.Test;

@WebTest
public class ArtistWebTest {

    @Test
    @ApiLogin
    @User
    void shouldAddArtistByAuthorizedUser() {
        String artistName = RandomDataUtils.randomArtistName();

        Selenide.open(MainPage.URL, MainPage.class)
                .goToArtists()
                .addArtist(artistName,
                        RandomDataUtils.randomArtistBiography(),
                        PhotoPaths.ARTIST
                )
                .checkArtist(artistName);
    }

    @Test
    @ApiLogin
    @User
    @Artist
    void shouldUpdateArtistByAuthorizedUser(ArtistJson artist) {
        String artistName = RandomDataUtils.randomArtistName();
        String randomBiography = RandomDataUtils.randomArtistBiography();
        Selenide.open(ArtistsPage.URL, ArtistsPage.class)
                .clickToArtist(artist.name())
                .editArtist()
                .setName(artistName)
                .setBiography(randomBiography)
                .setPhoto(PhotoPaths.IMAGE_TO_UPLOAD)
                .submitForm();
        new ArtistPage()
                .checkArtistName(artistName)
                .checkBiography(randomBiography)
                .checkAvatarImg(ResourceUtils.imageToDataUrl(PhotoPaths.IMAGE_TO_UPLOAD));
    }

    @Test
    void shouldNotBeAbleToAddArtistForUnauthorizedUser() {
        Selenide.open(ArtistsPage.URL, ArtistsPage.class)
                .checkAddButtonNotExist();
    }

    @Test
    @Artist
    void shouldFindArtistByName(ArtistJson artist) {
        Selenide.open(ArtistsPage.URL, ArtistsPage.class)
                .checkArtist(artist.name())
                .checkArtistsSize(1);
    }

    @Test
    @ApiLogin
    @User
    @Artists(count = 36)
    void shouldLoadArtistsByPageByAuthorizedUser() {
        Selenide.open(ArtistsPage.URL, ArtistsPage.class)
                .checkArtistsGreaterOrEqThan(18)
                .loadNextPage()
                .checkArtistsSize(36);
    }

    @Test
    @Artists(count = 36)
    void shouldLoadArtistsByPageByUnauthorizedUser() {
        Selenide.open(ArtistsPage.URL, ArtistsPage.class)
                .checkArtistsGreaterOrEqThan(18)
                .loadNextPage()
                .checkArtistsSize(36);
    }

    @Test
    @ApiLogin
    @User
    @Artist
    void shouldDisplayArtistDetails(ArtistJson artist) {
        Selenide.open(ArtistsPage.URL, ArtistsPage.class)
                .clickToArtist(artist.name())
                .checkArtistName(artist.name())
                .checkBiography(artist.biography())
                .checkAvatarImg(artist.photo());
    }
}
