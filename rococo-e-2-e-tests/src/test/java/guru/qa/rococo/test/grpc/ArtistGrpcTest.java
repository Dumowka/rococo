package guru.qa.rococo.test.grpc;

import guru.qa.rococo.jupiter.annotation.Artist;
import guru.qa.rococo.jupiter.annotation.Artists;
import guru.qa.rococo.jupiter.annotation.GrpcTest;
import guru.qa.rococo.jupiter.annotation.User;
import guru.qa.rococo.model.artist.ArtistJson;
import guru.qa.rococo.service.grpc.ArtistGrpcClient;
import guru.qa.rococo.service.impl.ArtistDbClient;
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
public class ArtistGrpcTest {

    private final ArtistGrpcClient artistGrpcClient = new ArtistGrpcClient();

    private final ArtistDbClient artistDbClient = new ArtistDbClient();

    @Test
    @Artists(count = 3)
    void shouldReturnedReturnNumberOfArtistsEqualToPageSize() {
        List<ArtistJson> artists = artistGrpcClient.getAllArtist(0, 2, null);
        assertEquals(2, artists.size());
    }

    @Test
    @Artists(count = 3)
    void shouldArtistFromFirstPageIsNotEqualsToArtistFromSecondPage() {
        List<ArtistJson> artistsFromFirstPage = artistGrpcClient.getAllArtist(0, 2, null);
        List<ArtistJson> artistsFromSecondPage = artistGrpcClient.getAllArtist(1, 2, null);
        assertNotEquals(artistsFromFirstPage, artistsFromSecondPage);
    }

    @Test
    @Artist
    void shouldReturnAllArtistsByName(ArtistJson artist) {
        List<ArtistJson> artists = artistGrpcClient.getAllArtist(0, 4, artist.name());
        assertTrue(artists.stream().anyMatch(artistJson -> artistJson.equals(artist)));
    }

    @Test
    void shouldReturnEmptyListWhenNoArtistsMatchFilter() {
        List<ArtistJson> response = artistGrpcClient.getAllArtist(0, 4, "non existing artist name");
        assertTrue(response.isEmpty());
    }

    @Test
    @Artist
    void shouldReturnArtistById(ArtistJson createdArtist) {
        ArtistJson response = artistGrpcClient.getArtistById(createdArtist.id());
        assertEquals(createdArtist, response);
    }

    @Test
    void shouldReturnErrorWhenGetArtistByNonExistingIdTest() {
        UUID nonExistingId = UUID.randomUUID();
        StatusRuntimeException exception = assertThrows(StatusRuntimeException.class, () -> artistGrpcClient.getArtistById(nonExistingId));
        assertTrue(exception.getMessage().contains("Artist not found with id: " + nonExistingId));
    }

    @Test
    @User
    void shouldCreateArtist() {
        ArtistJson createdArtist = artistGrpcClient.createArtist(ArtistJson.createArtist());
        ArtistJson expectedArtist = artistDbClient.getArtistById(createdArtist.id());
        assertEquals(expectedArtist, createdArtist);
    }

    @Test
    @Artist
    void shouldUpdateArtist(ArtistJson createdArtist) {
        String updatedName = RandomDataUtils.randomArtistName();
        ArtistJson updatedArtist = new ArtistJson(
                createdArtist.id(),
                updatedName,
                createdArtist.biography(),
                createdArtist.photo()
        );

        ArtistJson result = artistGrpcClient.updateArtist(updatedArtist);
        ArtistJson expectedArtist = artistDbClient.getArtistById(createdArtist.id());
        assertEquals(expectedArtist, result);
    }
}
