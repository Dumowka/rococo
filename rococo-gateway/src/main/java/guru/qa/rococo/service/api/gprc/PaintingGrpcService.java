package guru.qa.rococo.service.api.gprc;

import guru.qa.rococo.grpc.CreatePaintingRequest;
import guru.qa.rococo.grpc.GetAllPaintingsRequest;
import guru.qa.rococo.grpc.GetAllPaintingsResponse;
import guru.qa.rococo.grpc.GetPaintingByIdRequest;
import guru.qa.rococo.grpc.GetPaintingsByArtistIdRequest;
import guru.qa.rococo.grpc.Painting;
import guru.qa.rococo.grpc.RococoPaintingServiceGrpc;
import guru.qa.rococo.grpc.UpdatePaintingRequest;
import guru.qa.rococo.model.ArtistJson;
import guru.qa.rococo.model.MuseumJson;
import guru.qa.rococo.model.PaintingJson;
import guru.qa.rococo.service.api.ArtistService;
import guru.qa.rococo.service.api.MuseumService;
import guru.qa.rococo.service.api.PaintingService;
import io.grpc.StatusRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static guru.qa.rococo.model.PaintingJson.fromGrpcPainting;

@Service
public class PaintingGrpcService implements PaintingService {

    private final RococoPaintingServiceGrpc.RococoPaintingServiceBlockingStub paintingServiceStub;

    private final ArtistService artistService;
    private final MuseumService museumService;

    @Autowired
    public PaintingGrpcService(
            RococoPaintingServiceGrpc.RococoPaintingServiceBlockingStub paintingServiceStub,
            ArtistService artistService,
            MuseumService museumService
    ) {
        this.paintingServiceStub = paintingServiceStub;
        this.artistService = artistService;
        this.museumService = museumService;
    }

    @Override
    public Page<PaintingJson> getAllPainting(Pageable pageable, String title) {
        try {
            GetAllPaintingsRequest.Builder requestBuilder = GetAllPaintingsRequest.newBuilder()
                    .setPage(pageable.getPageNumber())
                    .setSize(pageable.getPageSize());

            if (title != null && !title.isEmpty()) {
                requestBuilder.setTitle(title);
            }

            GetAllPaintingsResponse response = paintingServiceStub.getAllPaintings(requestBuilder.build());

            List<PaintingJson> paintings = response.getPaintingsList().stream()
                    .map(this::fromGrpcPaintingWithMuseumAndArtist)
                    .toList();

            return new PageImpl<>(paintings, pageable, response.getPaintingsList().size());
        } catch (StatusRuntimeException e) {
            throw new RuntimeException("Error get all paintings: " + e.getStatus().getDescription(), e);
        }
    }

    @Override
    public PaintingJson getPaintingById(UUID id) {
        try {
            GetPaintingByIdRequest request = GetPaintingByIdRequest.newBuilder()
                    .setId(id.toString())
                    .build();

            Painting response = paintingServiceStub.getPaintingById(request);
            return fromGrpcPaintingWithMuseumAndArtist(response);
        } catch (StatusRuntimeException e) {
            throw new RuntimeException("Error get painting by id: " + e.getStatus().getDescription(), e);
        }
    }

    @Override
    public Page<PaintingJson> getPaintingsByArtistId(UUID artistId, Pageable pageable) {
        try {
            GetPaintingsByArtistIdRequest request = GetPaintingsByArtistIdRequest.newBuilder()
                    .setArtistId(artistId.toString())
                    .setPage(pageable.getPageNumber())
                    .setSize(pageable.getPageSize())
                    .build();

            GetAllPaintingsResponse response = paintingServiceStub.getPaintingsByArtistId(request);

            List<PaintingJson> paintings = response.getPaintingsList().stream()
                    .map(this::fromGrpcPaintingWithMuseumAndArtist)
                    .toList();

            return new PageImpl<>(paintings, pageable, response.getPaintingsList().size());
        } catch (StatusRuntimeException e) {
            throw new RuntimeException("Error get painting by artist id: " + e.getStatus().getDescription(), e);
        }
    }

    @Override
    public PaintingJson createPainting(PaintingJson paintingJson) {
        try {
            CreatePaintingRequest.Builder requestBuilder = CreatePaintingRequest.newBuilder()
                    .setTitle(paintingJson.title())
                    .setDescription(paintingJson.description() != null ? paintingJson.description() : "");

            if (paintingJson.artist() != null) {
                requestBuilder.setArtistId(paintingJson.artist().id().toString());
            } else {
                throw new IllegalArgumentException("Artist is required for a painting");
            }

            if (paintingJson.content() != null) {
                requestBuilder.setContent(paintingJson.content());
            }

            if (paintingJson.museum() != null && paintingJson.museum().id() != null) {
                requestBuilder.setMuseumId(paintingJson.museum().id().toString());
            }

            Painting painting = paintingServiceStub.createPainting(requestBuilder.build());
            return fromGrpcPaintingWithMuseumAndArtist(painting);
        } catch (StatusRuntimeException e) {
            throw new RuntimeException("Error create painting: " + e.getStatus().getDescription(), e);
        }
    }

    @Override
    public PaintingJson updatePainting(PaintingJson paintingJson) {
        try {
            UpdatePaintingRequest.Builder requestBuilder = UpdatePaintingRequest.newBuilder()
                    .setId(paintingJson.id().toString())
                    .setTitle(paintingJson.title())
                    .setDescription(paintingJson.description() != null ? paintingJson.description() : "");

            if (paintingJson.artist() != null) {
                requestBuilder.setArtistId(paintingJson.artist().id().toString());
            } else {
                throw new IllegalArgumentException("Artist is required for a painting");
            }

            if (paintingJson.content() != null) {
                requestBuilder.setContent(paintingJson.content());
            }

            if (paintingJson.museum() != null && paintingJson.museum().id() != null) {
                requestBuilder.setMuseumId(paintingJson.museum().id().toString());
            }

            Painting painting = paintingServiceStub.updatePainting(requestBuilder.build());
            return fromGrpcPaintingWithMuseumAndArtist(painting);
        } catch (StatusRuntimeException e) {
            throw new RuntimeException("Error update painting: " + e.getStatus().getDescription(), e);
        }
    }

    private PaintingJson fromGrpcPaintingWithMuseumAndArtist(Painting painting) {
        ArtistJson artist = !painting.getArtistId().isEmpty()
                ? artistService.getArtistById(UUID.fromString(painting.getArtistId()))
                : null;
        MuseumJson museum = !painting.getMuseumId().isEmpty()
                ? museumService.getMuseumById(UUID.fromString(painting.getMuseumId()))
                : null;

        return fromGrpcPainting(painting, museum, artist);
    }
}
