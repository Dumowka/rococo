package guru.qa.rococo.test.rest;

import guru.qa.rococo.jupiter.annotation.ApiLogin;
import guru.qa.rococo.jupiter.annotation.Artist;
import guru.qa.rococo.jupiter.annotation.Artists;
import guru.qa.rococo.jupiter.annotation.RestTest;
import guru.qa.rococo.jupiter.annotation.Token;
import guru.qa.rococo.jupiter.annotation.User;
import guru.qa.rococo.jupiter.extension.ApiLoginExtension;
import guru.qa.rococo.model.artist.ArtistJson;
import guru.qa.rococo.model.pageable.RestResponsePage;
import guru.qa.rococo.service.impl.ArtistDbClient;
import guru.qa.rococo.service.impl.GatewayApiClient;
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
public class ArtistRestTest {

    @RegisterExtension
    private static final ApiLoginExtension apiLoginExtension = ApiLoginExtension.restApiLoginExtension();

    private final GatewayApiClient gatewayApiClient = new GatewayApiClient();
    private final ArtistDbClient artistDbClient = new ArtistDbClient();

    @Test
    @Artists(count = 3)
    void shouldReturnedReturnNumberOfArtistsEqualToPageSize() {
        RestResponsePage<ArtistJson> artists = gatewayApiClient.getAllArtist(0, 2, null);
        assertEquals(2, artists.getSize());
    }

    @Test
    @Artists(count = 3)
    void shouldArtistFromFirstPageIsNotEqualsToArtistFromSecondPage() {
        RestResponsePage<ArtistJson> artistsFromFirstPage = gatewayApiClient.getAllArtist(0, 2, null);
        RestResponsePage<ArtistJson> artistsFromSecondPage = gatewayApiClient.getAllArtist(1, 2, null);
        assertNotEquals(artistsFromFirstPage.getContent(), artistsFromSecondPage.getContent());
    }

    @Test
    @Artist
    void shouldReturnAllArtistsByName(ArtistJson artist) {
        RestResponsePage<ArtistJson> artists = gatewayApiClient.getAllArtist(0, 4, artist.name());
        assertTrue(artists.stream().anyMatch(artistJson -> artistJson.equals(artist)));
    }

    @Test
    void shouldReturnEmptyListWhenNoArtistsMatchFilter() {
        RestResponsePage<ArtistJson> response = gatewayApiClient.getAllArtist(0, 4, "non existing artist name");
        assertTrue(response.getContent().isEmpty());
        assertEquals(0, response.getNumberOfElements());
    }

    @Test
    @Artist
    void shouldReturnArtistById(ArtistJson createdArtist) {
        ArtistJson response = gatewayApiClient.getArtistById(createdArtist.id());
        assertEquals(createdArtist, response);
    }

    @Test
    void shouldReturnErrorWhenGetArtistByNonExistingIdTest() {
        AssertionError assertionError = assertThrows(AssertionError.class, () -> gatewayApiClient.getArtistById(UUID.randomUUID()));
        assertTrue(assertionError.getMessage().contains("Response code: 401"));
    }

    @Test
    @User
    @ApiLogin
    void shouldCreateArtist(@Token String token) {
        ArtistJson createdArtist = gatewayApiClient.createArtist(token, ArtistJson.createArtist());
        ArtistJson expectedArtist = artistDbClient.getArtistById(createdArtist.id());
        assertEquals(expectedArtist, createdArtist);
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
    void shouldReturnErrorWhenCreateArtistWithIncorrectToken(String token) {
        AssertionError assertionError = assertThrows(AssertionError.class, () -> gatewayApiClient.createArtist(token, ArtistJson.createArtist()));
        assertTrue(assertionError.getMessage().contains("Response code: 401"));
    }

    @Test
    @ApiLogin
    @User
    @Artist
    void shouldUpdateArtist(@Token String token, ArtistJson createdArtist) {
        String updatedName = RandomDataUtils.randomArtistName();
        ArtistJson updatedArtist = new ArtistJson(
                createdArtist.id(),
                updatedName,
                createdArtist.biography(),
                createdArtist.photo()
        );

        ArtistJson result = gatewayApiClient.updateArtist(token, updatedArtist);
        ArtistJson expectedArtist = artistDbClient.getArtistById(createdArtist.id());
        assertEquals(expectedArtist, result);
    }

    @ParameterizedTest
    @MethodSource("incorrectTokens")
    @Artist
    void shouldReturnErrorWhenUpdateArtistWithIncorrectToken(String token, ArtistJson createdArtist) {
        String updatedName = RandomDataUtils.randomArtistName();
        ArtistJson updatedArtist = new ArtistJson(
                createdArtist.id(),
                updatedName,
                createdArtist.biography(),
                createdArtist.photo()
        );
        AssertionError assertionError = assertThrows(AssertionError.class, () -> gatewayApiClient.updateArtist(token, updatedArtist));
        assertTrue(assertionError.getMessage().contains("Response code: 401"));
    }
}
