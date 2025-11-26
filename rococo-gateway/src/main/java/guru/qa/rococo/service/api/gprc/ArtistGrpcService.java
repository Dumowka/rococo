package guru.qa.rococo.service.api.gprc;

import guru.qa.rococo.ex.NotFoundException;
import guru.qa.rococo.grpc.Artist;
import guru.qa.rococo.grpc.CreateArtistRequest;
import guru.qa.rococo.grpc.GetAllArtistsRequest;
import guru.qa.rococo.grpc.GetAllArtistsResponse;
import guru.qa.rococo.grpc.GetArtistByIdRequest;
import guru.qa.rococo.grpc.RococoArtistServiceGrpc;
import guru.qa.rococo.grpc.UpdateArtistRequest;
import guru.qa.rococo.model.ArtistJson;
import guru.qa.rococo.service.api.ArtistService;
import io.grpc.StatusRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static guru.qa.rococo.model.ArtistJson.fromGrpcArtist;

@Service
public class ArtistGrpcService implements ArtistService {

    private final RococoArtistServiceGrpc.RococoArtistServiceBlockingStub artistServiceStub;

    @Autowired
    public ArtistGrpcService(RococoArtistServiceGrpc.RococoArtistServiceBlockingStub artistServiceStub) {
        this.artistServiceStub = artistServiceStub;
    }

    @Override
    public Page<ArtistJson> getAllArtist(Pageable pageable, String name) {
        try {
            GetAllArtistsRequest.Builder requestBuilder = GetAllArtistsRequest.newBuilder()
                    .setPage(pageable.getPageNumber())
                    .setSize(pageable.getPageSize());

            if (name != null && !name.isEmpty()) {
                requestBuilder.setName(name);
            }

            GetAllArtistsResponse response = artistServiceStub.getAllArtists(requestBuilder.build());

            List<ArtistJson> artists = response.getArtistsList().stream()
                    .map(ArtistJson::fromGrpcArtist)
                    .toList();

            return new PageImpl<>(artists, pageable, artists.size());
        } catch (StatusRuntimeException e) {
            throw new RuntimeException("Error get all artists: " + e.getStatus().getDescription(), e);
        }
    }

    @Override
    public ArtistJson getArtistById(UUID id) {
        try {
            GetArtistByIdRequest request = GetArtistByIdRequest.newBuilder()
                    .setId(id.toString())
                    .build();

            Artist response = artistServiceStub.getArtistById(request);
            return fromGrpcArtist(response);
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == io.grpc.Status.Code.NOT_FOUND) {
                throw new NotFoundException("Artist not found with id: " + id);
            }
            throw new RuntimeException("Error get artist by id: " + e.getMessage(), e);
        }
    }

    @Override
    public ArtistJson createArtist(ArtistJson artistJson) {
        try {
            CreateArtistRequest.Builder requestBuilder = CreateArtistRequest.newBuilder()
                    .setName(artistJson.name())
                    .setBiography(artistJson.biography() != null ? artistJson.biography() : "");

            if (artistJson.photo() != null) {
                requestBuilder.setPhoto(artistJson.photo());
            }

            Artist response = artistServiceStub.createArtist(requestBuilder.build());
            return fromGrpcArtist(response);
        } catch (StatusRuntimeException e) {
            throw new RuntimeException("Error create artist: " + e.getStatus().getDescription(), e);
        }
    }

    @Override
    public ArtistJson updateArtist(ArtistJson artistJson) {
        try {
            UpdateArtistRequest.Builder requestBuilder = UpdateArtistRequest.newBuilder()
                    .setId(artistJson.id().toString())
                    .setName(artistJson.name())
                    .setBiography(artistJson.biography() != null ? artistJson.biography() : "");

            if (artistJson.photo() != null) {
                requestBuilder.setPhoto(artistJson.photo());
            }

            Artist response = artistServiceStub.updateArtist(requestBuilder.build());
            return fromGrpcArtist(response);
        } catch (StatusRuntimeException e) {
            throw new RuntimeException("Error update artist: " + e.getStatus().getDescription(), e);
        }
    }
}
