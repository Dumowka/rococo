package guru.qa.rococo.test.grpc;

import guru.qa.rococo.jupiter.annotation.GrpcTest;
import guru.qa.rococo.jupiter.annotation.Museum;
import guru.qa.rococo.jupiter.annotation.Museums;
import guru.qa.rococo.model.Countries;
import guru.qa.rococo.model.museum.CountryJson;
import guru.qa.rococo.model.museum.MuseumJson;
import guru.qa.rococo.service.grpc.MuseumGrpcClient;
import guru.qa.rococo.service.impl.CountryDbClient;
import guru.qa.rococo.service.impl.MuseumDbClient;
import guru.qa.rococo.utils.RandomDataUtils;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@GrpcTest
public class MuseumGrpcTest {

    private final MuseumGrpcClient museumGrpcClient = new MuseumGrpcClient();

    private final MuseumDbClient museumDbClient = new MuseumDbClient();
    private final CountryDbClient countryDbClient = new CountryDbClient();

    @Test
    @Museums(count = 3)
    void shouldReturnedReturnNumberOfMuseumsEqualToPageSize() {
        List<MuseumJson> museums = museumGrpcClient.getAllMuseum(0, 2, null);
        assertEquals(2, museums.size());
    }

    @Test
    @Museums(count = 3)
    void shouldMuseumFromFirstPageIsNotEqualsToMuseumFromSecondPage() {
        List<MuseumJson> museumsFromFirstPage = museumGrpcClient.getAllMuseum(0, 2, null);
        List<MuseumJson> museumsFromSecondPage = museumGrpcClient.getAllMuseum(1, 2, null);
        assertNotEquals(museumsFromFirstPage, museumsFromSecondPage);
    }

    @Test
    @Museum
    void shouldReturnAllMuseumsByTitle(MuseumJson museum) {
        List<MuseumJson> museums = museumGrpcClient.getAllMuseum(0, 4, museum.title());
        assertTrue(museums.stream().anyMatch(museumJson -> museumJson.equals(museum)));
    }

    @Test
    void shouldReturnEmptyListWhenNoMuseumsMatchFilter() {
        List<MuseumJson> response = museumGrpcClient.getAllMuseum(0, 4, "non existing museum title");
        assertTrue(response.isEmpty());
    }

    @Test
    @Museum
    void shouldReturnMuseumById(MuseumJson createdMuseum) {
        MuseumJson response = museumGrpcClient.getMuseumById(createdMuseum.id());
        assertEquals(createdMuseum, response);
    }

    @Test
    void shouldReturnErrorWhenGetMuseumByNonExistingIdTest() {
        UUID nonExistingId = UUID.randomUUID();
        StatusRuntimeException exception = assertThrows(StatusRuntimeException.class, () -> museumGrpcClient.getMuseumById(nonExistingId));
        assertTrue(exception.getMessage().contains("Museum not found with id: " + nonExistingId));
    }

    @Test
    void shouldCreateMuseum() {
        CountryJson country = CountryJson.fromEntity(countryDbClient.findByName(Countries.random().getName()));
        MuseumJson createdMuseum = museumGrpcClient.createMuseum(MuseumJson.createMuseum(country));
        MuseumJson expectedMuseum = museumDbClient.getMuseumById(createdMuseum.id());
        assertEquals(expectedMuseum, createdMuseum);
    }

    @Test
    @Museum
    void shouldUpdateMuseum(MuseumJson createdMuseum) {
        String updatedTitle = RandomDataUtils.randomMuseumName();
        MuseumJson updatedMuseum = new MuseumJson(
                createdMuseum.id(),
                updatedTitle,
                createdMuseum.description(),
                createdMuseum.photo(),
                createdMuseum.geo()
        );

        MuseumJson result = museumGrpcClient.updateMuseum(updatedMuseum);
        MuseumJson expectedMuseum = museumDbClient.getMuseumById(createdMuseum.id());
        assertEquals(expectedMuseum, result);
    }

    @Test
    void shouldReturnedReturnNumberOfCountriesEqualToPageSize() {
        List<CountryJson> countries = museumGrpcClient.getAllCountry();
        assertTrue(countries.size() >= 4);
    }

    @Test
    void shouldCountryFromFirstPageIsNotEqualsToCountryFromSecondPage() {
        List<CountryJson> countriesFromFirstPage = museumGrpcClient.getAllCountry(0, 2);
        List<CountryJson> countriesFromSecondPage = museumGrpcClient.getAllCountry(1, 2);
        assertNotEquals(countriesFromFirstPage, countriesFromSecondPage);
    }
}
