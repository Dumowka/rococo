package guru.qa.rococo.service.grpc;

import guru.qa.rococo.config.Config;
import guru.qa.rococo.grpc.Artist;
import guru.qa.rococo.grpc.CreateArtistRequest;
import guru.qa.rococo.grpc.GetAllArtistsRequest;
import guru.qa.rococo.grpc.GetArtistByIdRequest;
import guru.qa.rococo.grpc.RococoArtistServiceGrpc;
import guru.qa.rococo.grpc.UpdateArtistRequest;
import guru.qa.rococo.model.artist.ArtistJson;
import guru.qa.rococo.utils.GrpcConsoleInterceptor;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.Step;
import io.qameta.allure.grpc.AllureGrpc;

import java.util.List;
import java.util.UUID;

public class ArtistGrpcClient {

    private final Config CFG = Config.getInstance();

    private final Channel channel = ManagedChannelBuilder
            .forAddress(CFG.artistGrpcAddress(), CFG.artistGrpcPort())
            .intercept(new AllureGrpc())
            .intercept(new GrpcConsoleInterceptor())
            .usePlaintext()
            .build();

    private final RococoArtistServiceGrpc.RococoArtistServiceBlockingStub artistServiceStub
            = RococoArtistServiceGrpc.newBlockingStub(channel);

    @Step("Получение всех художников со страницы '{0}' в размере '{1}' и заголовком '{2}' через gRPC")
    public List<ArtistJson> getAllArtist(int page, int size, String name) {
        GetAllArtistsRequest request = GetAllArtistsRequest.newBuilder()
                .setPage(page)
                .setSize(size)
                .setName(name != null ? name : "")
                .build();

        return artistServiceStub.getAllArtists(request)
                .getArtistsList().stream()
                .map(ArtistJson::fromGrpcArtist)
                .toList();
    }

    @Step("Получение художника с id = '{0}' через gRPC")
    public ArtistJson getArtistById(UUID id) {
        GetArtistByIdRequest request = GetArtistByIdRequest.newBuilder()
                .setId(id.toString())
                .build();

        Artist artist = artistServiceStub.getArtistById(request);
        return ArtistJson.fromGrpcArtist(artist);
    }

    @Step("Создание художника через gRPC")
    public ArtistJson createArtist(ArtistJson artist) {
        CreateArtistRequest request = CreateArtistRequest.newBuilder()
                .setName(artist.name())
                .setBiography(artist.biography() != null ? artist.biography() : "")
                .setPhoto(artist.photo() != null ? artist.photo() : "")
                .build();

        Artist response = artistServiceStub.createArtist(request);
        return ArtistJson.fromGrpcArtist(response);
    }

    @Step("Обновление художника через gRPC")
    public ArtistJson updateArtist(ArtistJson artist) {
        UpdateArtistRequest request = UpdateArtistRequest.newBuilder()
                .setId(artist.id().toString())
                .setName(artist.name())
                .setBiography(artist.biography() != null ? artist.biography() : "")
                .setPhoto(artist.photo() != null ? artist.photo() : "")
                .build();

        Artist response = artistServiceStub.updateArtist(request);
        return ArtistJson.fromGrpcArtist(response);
    }
}
