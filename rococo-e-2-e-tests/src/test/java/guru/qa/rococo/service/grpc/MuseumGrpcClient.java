package guru.qa.rococo.service.grpc;

import guru.qa.rococo.config.Config;
import guru.qa.rococo.grpc.Country;
import guru.qa.rococo.grpc.CreateMuseumRequest;
import guru.qa.rococo.grpc.Geo;
import guru.qa.rococo.grpc.GetAllCountriesRequest;
import guru.qa.rococo.grpc.GetAllMuseumsRequest;
import guru.qa.rococo.grpc.GetMuseumByIdRequest;
import guru.qa.rococo.grpc.Museum;
import guru.qa.rococo.grpc.RococoCountryServiceGrpc;
import guru.qa.rococo.grpc.RococoMuseumServiceGrpc;
import guru.qa.rococo.grpc.UpdateMuseumRequest;
import guru.qa.rococo.model.museum.CountryJson;
import guru.qa.rococo.model.museum.MuseumJson;
import guru.qa.rococo.utils.GrpcConsoleInterceptor;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.Step;
import io.qameta.allure.grpc.AllureGrpc;

import java.util.List;
import java.util.UUID;

public class MuseumGrpcClient {

    private final Config CFG = Config.getInstance();

    private final Channel channel = ManagedChannelBuilder
            .forAddress(CFG.museumGrpcAddress(), CFG.museumGrpcPort())
            .intercept(new AllureGrpc())
            .intercept(new GrpcConsoleInterceptor())
            .usePlaintext()
            .build();

    private final RococoMuseumServiceGrpc.RococoMuseumServiceBlockingStub museumServiceStub
            = RococoMuseumServiceGrpc.newBlockingStub(channel);

    private final RococoCountryServiceGrpc.RococoCountryServiceBlockingStub countryBlockingStub
            = RococoCountryServiceGrpc.newBlockingStub(channel);

    @Step("Получение всех музеев со страницы '{0}' в размере '{1}' и заголовком '{2}' через gRPC")
    public List<MuseumJson> getAllMuseum(int page, int size, String title) {
        GetAllMuseumsRequest request = GetAllMuseumsRequest.newBuilder()
                .setPage(page)
                .setSize(size)
                .setTitle(title != null ? title : "")
                .build();

        return museumServiceStub.getAllMuseums(request)
                .getMuseumsList().stream()
                .map(MuseumJson::fromGrpcMuseum)
                .toList();
    }

    @Step("Получение музея с id = '{0}' через gRPC")
    public MuseumJson getMuseumById(UUID id) {
        GetMuseumByIdRequest request = GetMuseumByIdRequest.newBuilder()
                .setId(id.toString())
                .build();

        Museum museum = museumServiceStub.getMuseumById(request);
        return MuseumJson.fromGrpcMuseum(museum);
    }

    @Step("Создание музея через gRPC")
    public MuseumJson createMuseum(MuseumJson museum) {
        CreateMuseumRequest request = CreateMuseumRequest.newBuilder()
                .setTitle(museum.title())
                .setDescription(museum.description() != null ? museum.description() : "")
                .setPhoto(museum.photo() != null ? museum.photo() : "")
                .setGeo(Geo.newBuilder()
                        .setCity(museum.geo().city() != null ? museum.geo().city() : "")
                        .setCountry(Country.newBuilder()
                                .setId(museum.geo().country().id() != null ? museum.geo().country().id().toString() : "")
                                .setName(museum.geo().country().name() != null ? museum.geo().country().name() : "")
                                .build())
                ).build();

        Museum createdMuseum = museumServiceStub.createMuseum(request);
        return MuseumJson.fromGrpcMuseum(createdMuseum);
    }

    @Step("Обновление музея через gRPC")
    public MuseumJson updateMuseum(MuseumJson museum) {
        UpdateMuseumRequest request = UpdateMuseumRequest.newBuilder()
                .setId(museum.id().toString())
                .setTitle(museum.title())
                .setDescription(museum.description() != null ? museum.description() : "")
                .setPhoto(museum.photo() != null ? museum.photo() : "")
                .setGeo(Geo.newBuilder()
                        .setCity(museum.geo().city() != null ? museum.geo().city() : "")
                        .setCountry(Country.newBuilder()
                                .setId(museum.geo().country().id() != null ? museum.geo().country().id().toString() : "")
                                .setName(museum.geo().country().name() != null ? museum.geo().country().name() : "")
                                .build())
                ).build();

        Museum createdMuseum = museumServiceStub.updateMuseum(request);
        return MuseumJson.fromGrpcMuseum(createdMuseum);
    }

    @Step("Получение всех стран через gRPC")
    public List<CountryJson> getAllCountry() {
        return getAllCountry(0, 194);
    }

    @Step("Получение всех стран со страницы '{0}' в размере '{1}' через gRPC")
    public List<CountryJson> getAllCountry(int page, int size) {
        GetAllCountriesRequest request = GetAllCountriesRequest.newBuilder()
                .setPage(page)
                .setSize(size)
                .build();

        return countryBlockingStub.getAllCountries(request)
                .getCountriesList().stream()
                .map(country -> new CountryJson(
                        UUID.fromString(country.getId()),
                        country.getName()
                )).toList();
    }
}
