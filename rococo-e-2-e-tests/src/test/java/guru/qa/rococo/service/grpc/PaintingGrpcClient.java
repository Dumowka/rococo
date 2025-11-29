package guru.qa.rococo.service.grpc;

import guru.qa.rococo.config.Config;
import guru.qa.rococo.grpc.CreatePaintingRequest;
import guru.qa.rococo.grpc.GetAllPaintingsRequest;
import guru.qa.rococo.grpc.GetPaintingByIdRequest;
import guru.qa.rococo.grpc.GetPaintingsByArtistIdRequest;
import guru.qa.rococo.grpc.Painting;
import guru.qa.rococo.grpc.RococoPaintingServiceGrpc;
import guru.qa.rococo.grpc.UpdatePaintingRequest;
import guru.qa.rococo.model.painting.PaintingJson;
import guru.qa.rococo.service.impl.ArtistDbClient;
import guru.qa.rococo.service.impl.MuseumDbClient;
import guru.qa.rococo.utils.GrpcConsoleInterceptor;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.Step;
import io.qameta.allure.grpc.AllureGrpc;

import java.util.List;
import java.util.UUID;

public class PaintingGrpcClient {

    private final Config CFG = Config.getInstance();

    private final Channel channel = ManagedChannelBuilder
            .forAddress(CFG.paintingGrpcAddress(), CFG.paintingGrpcPort())
            .intercept(new AllureGrpc())
            .intercept(new GrpcConsoleInterceptor())
            .usePlaintext()
            .build();



    private final MuseumDbClient museumDbClient = new MuseumDbClient();
    private final ArtistDbClient artistDbClient = new ArtistDbClient();

    private final RococoPaintingServiceGrpc.RococoPaintingServiceBlockingStub paintingServiceStub
            = RococoPaintingServiceGrpc.newBlockingStub(channel);

    @Step("Получение всех картин со страницы '{0}' в размере '{1}' и заголовком '{2}' через gRPC")
    public List<PaintingJson> getAllPainting(int page, int size, String title) {
        GetAllPaintingsRequest request = GetAllPaintingsRequest.newBuilder()
                .setPage(page)
                .setSize(size)
                .setTitle(title != null ? title : "")
                .build();

        return paintingServiceStub.getAllPaintings(request)
                .getPaintingsList().stream()
                .map(this::convertFromPaintingEntity)
                .toList();
    }

    @Step("Получение картины с id = '{0}' через gRPC")
    public PaintingJson getPaintingById(UUID id) {
        GetPaintingByIdRequest request = GetPaintingByIdRequest.newBuilder()
                .setId(id.toString())
                .build();

        Painting painting = paintingServiceStub.getPaintingById(request);
        return convertFromPaintingEntity(painting);
    }

    @Step("Получение всех картин со страницы '{0}' в размере '{1}' для художника с идентификатором '{2}' через gRPC")
    public List<PaintingJson> getPaintingsByArtistId(int page, int size, UUID id) {
        GetPaintingsByArtistIdRequest request = GetPaintingsByArtistIdRequest.newBuilder()
                .setPage(page)
                .setSize(size)
                .setArtistId(id.toString())
                .build();

        return paintingServiceStub.getPaintingsByArtistId(request)
                .getPaintingsList().stream()
                .map(this::convertFromPaintingEntity)
                .toList();
    }

    @Step("Создание картины через gRPC")
    public PaintingJson createPainting(PaintingJson paintingJson) {
        CreatePaintingRequest.Builder requestBuilder = CreatePaintingRequest.newBuilder()
                .setTitle(paintingJson.title())
                .setDescription(paintingJson.description() != null ? paintingJson.description() : "")
                .setContent(paintingJson.content() != null ? paintingJson.content() : "");

        if (paintingJson.artist() != null && paintingJson.artist().id() != null) {
            requestBuilder.setArtistId(paintingJson.artist().id().toString());
        }

        if (paintingJson.museum() != null && paintingJson.museum().id() != null) {
            requestBuilder.setMuseumId(paintingJson.museum().id().toString());
        }

        Painting painting = paintingServiceStub.createPainting(requestBuilder.build());
        return convertFromPaintingEntity(painting);
    }

    @Step("Обновление картины через gRPC")
    public PaintingJson updatePainting(PaintingJson paintingJson) {
        UpdatePaintingRequest.Builder requestBuilder = UpdatePaintingRequest.newBuilder()
                .setId(paintingJson.id().toString())
                .setTitle(paintingJson.title())
                .setDescription(paintingJson.description() != null ? paintingJson.description() : "")
                .setContent(paintingJson.content() != null ? paintingJson.content() : "");

        if (paintingJson.artist() != null && paintingJson.artist().id() != null) {
            requestBuilder.setArtistId(paintingJson.artist().id().toString());
        }

        if (paintingJson.museum() != null && paintingJson.museum().id() != null) {
            requestBuilder.setMuseumId(paintingJson.museum().id().toString());
        }

        Painting painting = paintingServiceStub.updatePainting(requestBuilder.build());
        return convertFromPaintingEntity(painting);
    }

    private PaintingJson convertFromPaintingEntity(Painting painting) {
        return PaintingJson.fromGrpcPainting(
                painting,
                painting.getMuseumId() != null ? museumDbClient.getMuseumById(UUID.fromString(painting.getMuseumId())) : null,
                painting.getArtistId() != null ? artistDbClient.getArtistById(UUID.fromString(painting.getArtistId())) : null
        );
    }
}
