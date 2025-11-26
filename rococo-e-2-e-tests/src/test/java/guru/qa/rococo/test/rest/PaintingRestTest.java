package guru.qa.rococo.test.rest;

import guru.qa.rococo.jupiter.annotation.ApiLogin;
import guru.qa.rococo.jupiter.annotation.Artist;
import guru.qa.rococo.jupiter.annotation.Museum;
import guru.qa.rococo.jupiter.annotation.Painting;
import guru.qa.rococo.jupiter.annotation.Paintings;
import guru.qa.rococo.jupiter.annotation.RestTest;
import guru.qa.rococo.jupiter.annotation.Token;
import guru.qa.rococo.jupiter.annotation.User;
import guru.qa.rococo.jupiter.extension.ApiLoginExtension;
import guru.qa.rococo.model.artist.ArtistJson;
import guru.qa.rococo.model.museum.CountryJson;
import guru.qa.rococo.model.museum.MuseumJson;
import guru.qa.rococo.model.pageable.RestResponsePage;
import guru.qa.rococo.model.painting.PaintingJson;
import guru.qa.rococo.service.impl.ArtistDbClient;
import guru.qa.rococo.service.impl.GatewayApiClient;
import guru.qa.rococo.service.impl.MuseumDbClient;
import guru.qa.rococo.service.impl.PaintingDbClient;
import guru.qa.rococo.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RestTest
public class PaintingRestTest {

    @RegisterExtension
    private static final ApiLoginExtension apiLoginExtension = ApiLoginExtension.restApiLoginExtension();

    private final GatewayApiClient gatewayApiClient = new GatewayApiClient();
    private final PaintingDbClient paintingDbClient = new PaintingDbClient();
    private final MuseumDbClient museumDbClient = new MuseumDbClient();
    private final ArtistDbClient artistDbClient = new ArtistDbClient();

    @Test
    @Paintings(count = 3)
    void shouldReturnedReturnNumberOfPaintingsEqualToPageSize() {
        RestResponsePage<PaintingJson> museums = gatewayApiClient.getAllPainting(0, 2, null);
        assertEquals(2, museums.getSize());
    }

    @Test
    @Paintings(count = 3)
    void shouldPaintingFromFirstPageIsNotEqualsToPaintingFromSecondPage() {
        RestResponsePage<PaintingJson> museumsFromFirstPage = gatewayApiClient.getAllPainting(0, 2, null);
        RestResponsePage<PaintingJson> museumsFromSecondPage = gatewayApiClient.getAllPainting(1, 2, null);
        assertNotEquals(museumsFromFirstPage.getContent(), museumsFromSecondPage.getContent());
    }

    @Test
    @Painting
    void shouldReturnAllPaintingsByTitle(PaintingJson museum) {
        RestResponsePage<PaintingJson> museums = gatewayApiClient.getAllPainting(0, 4, museum.title());
        assertTrue(museums.stream().anyMatch(museumJson -> museumJson.equals(museum)));
    }

    @Test
    void shouldReturnEmptyListWhenNoPaintingsMatchFilter() {
        RestResponsePage<PaintingJson> response = gatewayApiClient.getAllPainting(0, 4, "non existing museum title");
        assertTrue(response.getContent().isEmpty());
        assertEquals(0, response.getNumberOfElements());
    }

    @Test
    @Painting
    void shouldReturnPaintingById(PaintingJson createdPainting) {
        PaintingJson response = gatewayApiClient.getPaintingById(createdPainting.id());
        assertEquals(createdPainting, response);
    }

    @Test
    void shouldReturnErrorWhenGetPaintingByNonExistingId() {
        AssertionError assertionError = assertThrows(AssertionError.class, () -> gatewayApiClient.getPaintingById(UUID.randomUUID()));
        assertTrue(assertionError.getMessage().contains("Response code: 401"));
    }

    @Test
    @Painting
    void shouldReturnPaintingByArtistId(PaintingJson createdPainting) {
        RestResponsePage<PaintingJson> response = gatewayApiClient.getPaintingsByArtistId(0, 4, createdPainting.artist().id());
        assertEquals(1, response.getContent().size());
        assertTrue(response.getContent().stream().anyMatch(p -> p.equals(createdPainting)));
    }

    @Test
    void shouldReturnEmptyListWhenGetPaintingByNonExistingArtistId() {
        RestResponsePage<PaintingJson> response = gatewayApiClient.getPaintingsByArtistId(0, 4, UUID.randomUUID());
        assertTrue(response.getContent().isEmpty());
        assertEquals(0, response.getNumberOfElements());
    }

    @Test
    @User
    @ApiLogin
    @Museum
    @Artist
    void shouldCreatePainting(@Token String token, MuseumJson museum, ArtistJson artist) {
        PaintingJson createdPainting = gatewayApiClient.createPainting(token, PaintingJson.createPainting(museum, artist));
        PaintingJson expectedPainting = paintingDbClient.getPaintingById(createdPainting.id());
        MuseumJson expectedMuseum = museumDbClient.getMuseumById(createdPainting.museum().id());
        ArtistJson expectedArtist = artistDbClient.getArtistById(createdPainting.artist().id());
        assertEquals(expectedPainting.addMuseumAndArtist(expectedMuseum, expectedArtist), createdPainting);
    }

    private static Stream<String> incorrectTokens() {
        return Stream.of(
                "invalid-token",
                "",
                null
        );
    }

    @ParameterizedTest
    @MethodSource("incorrectTokens")
    @Museum
    @Artist
    void shouldReturnErrorWhenCreatePaintingWithIncorrectToken(String token, MuseumJson museum, ArtistJson artist) {
        AssertionError assertionError = assertThrows(AssertionError.class, () -> gatewayApiClient.createPainting(token, PaintingJson.createPainting(museum, artist)));
        assertTrue(assertionError.getMessage().contains("Response code: 401"));
    }

    @Test
    @ApiLogin
    @User
    @Painting
    void shouldUpdatePainting(@Token String token, PaintingJson createdPainting) {
        String updatedTitle = RandomDataUtils.randomPaintingName();
        PaintingJson updatedPainting = new PaintingJson(
                createdPainting.id(),
                updatedTitle,
                createdPainting.description(),
                createdPainting.content(),
                createdPainting.museum(),
                createdPainting.artist()
        );

        PaintingJson result = gatewayApiClient.updatePainting(token, updatedPainting);
        PaintingJson expectedPainting = paintingDbClient.getPaintingById(createdPainting.id());
        MuseumJson expectedMuseum = museumDbClient.getMuseumById(updatedPainting.museum().id());
        ArtistJson expectedArtist = artistDbClient.getArtistById(updatedPainting.artist().id());

        assertEquals(expectedPainting.addMuseumAndArtist(expectedMuseum, expectedArtist), result);
    }

    @ParameterizedTest
    @MethodSource("incorrectTokens")
    @Painting
    void shouldReturnErrorWhenUpdatePaintingWithIncorrectToken(String token, PaintingJson createdPainting) {
        String updatedTitle = RandomDataUtils.randomPaintingName();
        PaintingJson updatedPainting = new PaintingJson(
                createdPainting.id(),
                updatedTitle,
                createdPainting.description(),
                createdPainting.content(),
                createdPainting.museum(),
                createdPainting.artist()
        );
        AssertionError assertionError = assertThrows(AssertionError.class, () -> gatewayApiClient.updatePainting(token, updatedPainting));
        assertTrue(assertionError.getMessage().contains("Response code: 401"));
    }

    @Test
    @User
    @ApiLogin
    void shouldReturnedReturnNumberOfCountriesEqualToPageSize(@Token String token) {
        RestResponsePage<CountryJson> countries = gatewayApiClient.getAllCountry(token, 0, 2, null);
        assertEquals(2, countries.getSize());
    }

    @Test
    @User
    @ApiLogin
    void shouldCountryFromFirstPageIsNotEqualsToCountryFromSecondPage(@Token String token) {
        RestResponsePage<CountryJson> countriesFromFirstPage = gatewayApiClient.getAllCountry(token, 0, 2, null);
        RestResponsePage<CountryJson> countriesFromSecondPage = gatewayApiClient.getAllCountry(token, 1, 2, null);
        assertNotEquals(countriesFromFirstPage.getContent(), countriesFromSecondPage.getContent());
    }

    @ParameterizedTest
    @MethodSource("incorrectTokens")
    @User
    @ApiLogin
    void shouldReturnErrorWhenGetAllCountriesWithIncorrectToken(String token) {
        AssertionError assertionError = assertThrows(AssertionError.class, () -> gatewayApiClient.getAllCountry(token, 0, 4, null));
        assertTrue(assertionError.getMessage().contains("Response code: 401"));
    }
}
