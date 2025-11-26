package guru.qa.rococo.test.rest;

import guru.qa.rococo.jupiter.annotation.ApiLogin;
import guru.qa.rococo.jupiter.annotation.Museum;
import guru.qa.rococo.jupiter.annotation.Museums;
import guru.qa.rococo.jupiter.annotation.RestTest;
import guru.qa.rococo.jupiter.annotation.Token;
import guru.qa.rococo.jupiter.annotation.User;
import guru.qa.rococo.jupiter.extension.ApiLoginExtension;
import guru.qa.rococo.model.Countries;
import guru.qa.rococo.model.museum.CountryJson;
import guru.qa.rococo.model.museum.MuseumJson;
import guru.qa.rococo.model.pageable.RestResponsePage;
import guru.qa.rococo.service.impl.CountryDbClient;
import guru.qa.rococo.service.impl.GatewayApiClient;
import guru.qa.rococo.service.impl.MuseumDbClient;
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
public class MuseumRestTest {
    
    @RegisterExtension
    private static final ApiLoginExtension apiLoginExtension = ApiLoginExtension.restApiLoginExtension();

    private final GatewayApiClient gatewayApiClient = new GatewayApiClient();
    private final MuseumDbClient museumDbClient = new MuseumDbClient();
    private final CountryDbClient countryDbClient = new CountryDbClient();

    @Test
    @Museums(count = 3)
    void shouldReturnedReturnNumberOfMuseumsEqualToPageSize() {
        RestResponsePage<guru.qa.rococo.model.museum.MuseumJson> museums = gatewayApiClient.getAllMuseum(0, 2, null);
        assertEquals(2, museums.getSize());
    }

    @Test
    @Museums(count = 3)
    void shouldMuseumFromFirstPageIsNotEqualsToMuseumFromSecondPage() {
        RestResponsePage<MuseumJson> museumsFromFirstPage = gatewayApiClient.getAllMuseum(0, 2, null);
        RestResponsePage<MuseumJson> museumsFromSecondPage = gatewayApiClient.getAllMuseum(1, 2, null);
        assertNotEquals(museumsFromFirstPage.getContent(), museumsFromSecondPage.getContent());
    }

    @Test
    @Museum
    void shouldReturnAllMuseumsByTitle(MuseumJson museum) {
        RestResponsePage<MuseumJson> museums = gatewayApiClient.getAllMuseum(0, 4, museum.title());
        assertTrue(museums.stream().anyMatch(museumJson -> museumJson.equals(museum)));
    }

    @Test
    void shouldReturnEmptyListWhenNoMuseumsMatchFilter() {
        RestResponsePage<MuseumJson> response = gatewayApiClient.getAllMuseum(0, 4, "non existing museum title");
        assertTrue(response.getContent().isEmpty());
        assertEquals(0, response.getNumberOfElements());
    }

    @Test
    @Museum
    void shouldReturnMuseumById(MuseumJson createdMuseum) {
        MuseumJson response = gatewayApiClient.getMuseumById(createdMuseum.id());
        assertEquals(createdMuseum, response);
    }

    @Test
    void shouldReturnErrorWhenGetMuseumByNonExistingIdTest() {
        AssertionError assertionError = assertThrows(AssertionError.class, () -> gatewayApiClient.getMuseumById(UUID.randomUUID()));
        assertTrue(assertionError.getMessage().contains("Response code: 401"));
    }

    @Test
    @User
    @ApiLogin
    void shouldCreateMuseum(@Token String token) {
        CountryJson country = CountryJson.fromEntity(countryDbClient.findByName(Countries.random().getName()));
        MuseumJson createdMuseum = gatewayApiClient.createMuseum(token, MuseumJson.createMuseum(country));
        MuseumJson expectedMuseum = museumDbClient.getMuseumById(createdMuseum.id());
        assertEquals(expectedMuseum, createdMuseum);
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
    void shouldReturnErrorWhenCreateMuseumWithIncorrectToken(String token) {
        CountryJson country = CountryJson.fromEntity(countryDbClient.findByName(Countries.random().getName()));
        AssertionError assertionError = assertThrows(AssertionError.class, () -> gatewayApiClient.createMuseum(token, MuseumJson.createMuseum(country)));
        assertTrue(assertionError.getMessage().contains("Response code: 401"));
    }

    @Test
    @ApiLogin
    @User
    @Museum
    void shouldUpdateMuseum(@Token String token, MuseumJson createdMuseum) {
        String updatedTitle = RandomDataUtils.randomMuseumName();
        MuseumJson updatedMuseum = new MuseumJson(
                createdMuseum.id(),
                updatedTitle,
                createdMuseum.description(),
                createdMuseum.photo(),
                createdMuseum.geo()
        );

        MuseumJson result = gatewayApiClient.updateMuseum(token, updatedMuseum);
        MuseumJson expectedMuseum = museumDbClient.getMuseumById(createdMuseum.id());
        assertEquals(expectedMuseum, result);
    }

    @ParameterizedTest
    @MethodSource("incorrectTokens")
    @Museum
    void shouldReturnErrorWhenUpdateMuseumWithIncorrectToken(String token, MuseumJson createdMuseum) {
        String updatedTitle = RandomDataUtils.randomMuseumName();
        MuseumJson updatedMuseum = new MuseumJson(
                createdMuseum.id(),
                updatedTitle,
                createdMuseum.description(),
                createdMuseum.photo(),
                createdMuseum.geo()
        );
        AssertionError assertionError = assertThrows(AssertionError.class, () -> gatewayApiClient.updateMuseum(token, updatedMuseum));
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
