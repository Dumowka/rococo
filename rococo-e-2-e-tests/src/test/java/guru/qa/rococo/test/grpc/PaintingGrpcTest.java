package guru.qa.rococo.test.grpc;

import guru.qa.rococo.jupiter.annotation.Artist;
import guru.qa.rococo.jupiter.annotation.GrpcTest;
import guru.qa.rococo.jupiter.annotation.Museum;
import guru.qa.rococo.jupiter.annotation.Painting;
import guru.qa.rococo.jupiter.annotation.Paintings;
import guru.qa.rococo.model.artist.ArtistJson;
import guru.qa.rococo.model.museum.MuseumJson;
import guru.qa.rococo.model.painting.PaintingJson;
import guru.qa.rococo.service.grpc.PaintingGrpcClient;
import guru.qa.rococo.service.impl.ArtistDbClient;
import guru.qa.rococo.service.impl.MuseumDbClient;
import guru.qa.rococo.service.impl.PaintingDbClient;
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
public class PaintingGrpcTest {

    private final PaintingGrpcClient paintingGrpcClient = new PaintingGrpcClient();

    private final PaintingDbClient paintingDbClient = new PaintingDbClient();
    private final MuseumDbClient museumDbClient = new MuseumDbClient();
    private final ArtistDbClient artistDbClient = new ArtistDbClient();

    @Test
    @Paintings(count = 3)
    void shouldReturnedReturnNumberOfPaintingsEqualToPageSize() {
        List<PaintingJson> museums = paintingGrpcClient.getAllPainting(0, 2, null);
        assertEquals(2, museums.size());
    }

    @Test
    @Paintings(count = 3)
    void shouldPaintingFromFirstPageIsNotEqualsToPaintingFromSecondPage() {
        List<PaintingJson> museumsFromFirstPage = paintingGrpcClient.getAllPainting(0, 2, null);
        List<PaintingJson> museumsFromSecondPage = paintingGrpcClient.getAllPainting(1, 2, null);
        assertNotEquals(museumsFromFirstPage, museumsFromSecondPage);
    }

    @Test
    @Painting
    void shouldReturnAllPaintingsByTitle(PaintingJson museum) {
        List<PaintingJson> museums = paintingGrpcClient.getAllPainting(0, 4, museum.title());
        assertTrue(museums.stream().anyMatch(museumJson -> museumJson.equals(museum)));
    }

    @Test
    void shouldReturnEmptyListWhenNoPaintingsMatchFilter() {
        List<PaintingJson> response = paintingGrpcClient.getAllPainting(0, 4, "non existing museum title");
        assertTrue(response.isEmpty());
    }

    @Test
    @Painting
    void shouldReturnPaintingById(PaintingJson createdPainting) {
        PaintingJson response = paintingGrpcClient.getPaintingById(createdPainting.id());
        assertEquals(createdPainting, response);
    }

    @Test
    void shouldReturnErrorWhenGetPaintingByNonExistingId() {
        UUID nonExistingId = UUID.randomUUID();
        StatusRuntimeException exception = assertThrows(StatusRuntimeException.class, () -> paintingGrpcClient.getPaintingById(nonExistingId));
        assertTrue(exception.getMessage().contains("Painting not found with id: " + nonExistingId));
    }

    @Test
    @Painting
    void shouldReturnPaintingByArtistId(PaintingJson createdPainting) {
        List<PaintingJson> response = paintingGrpcClient.getPaintingsByArtistId(0, 4, createdPainting.artist().id());
        assertEquals(1, response.size());
        assertTrue(response.stream().anyMatch(p -> p.equals(createdPainting)));
    }

    @Test
    void shouldReturnEmptyListWhenGetPaintingByNonExistingArtistId() {
        List<PaintingJson> response = paintingGrpcClient.getPaintingsByArtistId(0, 4, UUID.randomUUID());
        assertTrue(response.isEmpty());
    }

    @Test
    @Museum
    @Artist
    void shouldCreatePainting(MuseumJson museum, ArtistJson artist) {
        PaintingJson createdPainting = paintingGrpcClient.createPainting(PaintingJson.createPainting(museum, artist));
        PaintingJson expectedPainting = paintingDbClient.getPaintingById(createdPainting.id());
        MuseumJson expectedMuseum = museumDbClient.getMuseumById(createdPainting.museum().id());
        ArtistJson expectedArtist = artistDbClient.getArtistById(createdPainting.artist().id());
        assertEquals(expectedPainting.addMuseumAndArtist(expectedMuseum, expectedArtist), createdPainting);
    }

    @Test
    @Painting
    void shouldUpdatePainting(PaintingJson createdPainting) {
        String updatedTitle = RandomDataUtils.randomPaintingName();
        PaintingJson updatedPainting = new PaintingJson(
                createdPainting.id(),
                updatedTitle,
                createdPainting.description(),
                createdPainting.content(),
                createdPainting.museum(),
                createdPainting.artist()
        );

        PaintingJson result = paintingGrpcClient.updatePainting(updatedPainting);
        PaintingJson expectedPainting = paintingDbClient.getPaintingById(createdPainting.id());
        MuseumJson expectedMuseum = museumDbClient.getMuseumById(updatedPainting.museum().id());
        ArtistJson expectedArtist = artistDbClient.getArtistById(updatedPainting.artist().id());

        assertEquals(expectedPainting.addMuseumAndArtist(expectedMuseum, expectedArtist), result);
    }
}
