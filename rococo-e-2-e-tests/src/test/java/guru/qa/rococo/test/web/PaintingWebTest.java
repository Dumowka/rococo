package guru.qa.rococo.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.rococo.jupiter.annotation.ApiLogin;
import guru.qa.rococo.jupiter.annotation.Artist;
import guru.qa.rococo.jupiter.annotation.Museum;
import guru.qa.rococo.jupiter.annotation.Painting;
import guru.qa.rococo.jupiter.annotation.Paintings;
import guru.qa.rococo.jupiter.annotation.User;
import guru.qa.rococo.jupiter.annotation.WebTest;
import guru.qa.rococo.model.PhotoPaths;
import guru.qa.rococo.model.artist.ArtistJson;
import guru.qa.rococo.model.painting.PaintingJson;
import guru.qa.rococo.page.MainPage;
import guru.qa.rococo.page.PaintingPage;
import guru.qa.rococo.page.PaintingsPage;
import guru.qa.rococo.utils.RandomDataUtils;
import guru.qa.rococo.utils.ResourceUtils;
import org.junit.jupiter.api.Test;

@WebTest
public class PaintingWebTest {

    @Test
    @User
    @ApiLogin
    @Artist
    @Museum
    void shouldAddPaintingByAuthorizedUser() {
        String paintingTitle = RandomDataUtils.randomPaintingName();
        String description = RandomDataUtils.randomSentence(20);

        Selenide.open(MainPage.URL, MainPage.class)
                .goToPaintings()
                .addPainting(
                        paintingTitle,
                        description,
                        PhotoPaths.PAINTING
                )
                .checkPainting(paintingTitle);
    }

    @Test
    @User
    @ApiLogin
    @Painting
    @Artist
    @Museum
    void shouldUpdatePaintingByAuthorizedUser(PaintingJson painting, ArtistJson artist) {
        String newDescription = RandomDataUtils.randomSentence(20);
        String newTitle = RandomDataUtils.randomPaintingName();
        Selenide.open(PaintingsPage.URL, PaintingsPage.class)
                .clickToPainting(painting.title())
                .editPainting()
                .setTitle(newTitle)
                .setContent(PhotoPaths.IMAGE_TO_UPLOAD)
                .setArtist(artist.name())
                .setDescription(newDescription)
                .submitForm();
        new PaintingPage()
                .checkPaintingTitle(newTitle)
                .checkArtistName(artist.name())
                .checkDescription(newDescription)
                .checkPaintingImg(ResourceUtils.imageToDataUrl(PhotoPaths.IMAGE_TO_UPLOAD));
    }

    @Test
    void shouldNotBeAbleToAddPaintingForUnauthorizedUser() {
        Selenide.open(PaintingsPage.URL, PaintingsPage.class)
                .checkAddButtonNotExist();
    }

    @Test
    @Painting
    void shouldFindPaintingByTitle(PaintingJson painting) {
        Selenide.open(PaintingsPage.URL, PaintingsPage.class)
                .checkPainting(painting.title())
                .checkPaintingsSize(1);
    }

    @Test
    @User
    @ApiLogin
    @Paintings(count = 18)
    void shouldLoadPaintingsByPageByAuthorizedUser() {
        Selenide.open(PaintingsPage.URL, PaintingsPage.class)
                .checkPaintingsSize(9)
                .loadNextPage()
                .checkPaintingsSize(18);
    }

    @Test
    @User
    @ApiLogin
    @Paintings(count = 18)
    void shouldLoadPaintingsByPageByUnauthorizedUser() {
        Selenide.open(PaintingsPage.URL, PaintingsPage.class)
                .checkPaintingsSize(9)
                .loadNextPage()
                .checkPaintingsSize(18);
    }

    @Test
    @Painting
    void shouldDisplayPaintingDetails(PaintingJson painting) {
        Selenide.open(PaintingsPage.URL, PaintingsPage.class)
                .clickToPainting(painting.title())
                .checkPaintingTitle(painting.title())
                .checkDescription(painting.description())
                .checkArtistName(painting.artist().name())
                .checkPaintingImg(painting.content());
    }

    @Test
    @Painting
    void shouldFilterPaintingsByTitle(PaintingJson painting) {
        Selenide.open(PaintingsPage.URL, PaintingsPage.class)
                .checkPainting(painting.title());
    }
}
