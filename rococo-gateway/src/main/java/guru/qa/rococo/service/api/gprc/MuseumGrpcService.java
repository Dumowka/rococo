package guru.qa.rococo.service.api.gprc;

import guru.qa.rococo.grpc.Country;
import guru.qa.rococo.grpc.CreateMuseumRequest;
import guru.qa.rococo.grpc.Geo;
import guru.qa.rococo.grpc.GetAllMuseumsRequest;
import guru.qa.rococo.grpc.GetAllMuseumsResponse;
import guru.qa.rococo.grpc.GetMuseumByIdRequest;
import guru.qa.rococo.grpc.Museum;
import guru.qa.rococo.grpc.RococoMuseumServiceGrpc;
import guru.qa.rococo.grpc.UpdateMuseumRequest;
import guru.qa.rococo.model.MuseumJson;
import guru.qa.rococo.service.api.MuseumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static guru.qa.rococo.model.MuseumJson.fromGrpcMuseum;

@Service
public class MuseumGrpcService implements MuseumService {

    private final RococoMuseumServiceGrpc.RococoMuseumServiceBlockingStub museumServiceStub;

    @Autowired
    public MuseumGrpcService(RococoMuseumServiceGrpc.RococoMuseumServiceBlockingStub museumServiceStub) {
        this.museumServiceStub = museumServiceStub;
    }

    @Override
    public Page<MuseumJson> getAllMuseums(Pageable pageable, String title) {
        GetAllMuseumsRequest.Builder requestBuilder = GetAllMuseumsRequest.newBuilder()
                .setPage(pageable.getPageNumber())
                .setSize(pageable.getPageSize());

        if (title != null && !title.isEmpty()) {
            requestBuilder.setTitle(title);
        }

        GetAllMuseumsResponse response = museumServiceStub.getAllMuseums(requestBuilder.build());

        List<MuseumJson> museums = response.getMuseumsList().stream()
                .map(MuseumJson::fromGrpcMuseum)
                .toList();

        return new PageImpl<>(museums, pageable, response.getPageSize());
    }

    @Override
    public MuseumJson getMuseumById(UUID id) {
        GetMuseumByIdRequest request = GetMuseumByIdRequest.newBuilder()
                .setId(id.toString())
                .build();

        Museum response = museumServiceStub.getMuseumById(request);
        return fromGrpcMuseum(response);
    }

    @Override
    public MuseumJson createMuseum(MuseumJson museumJson) {
        CreateMuseumRequest request = CreateMuseumRequest.newBuilder()
                .setTitle(museumJson.title())
                .setDescription(museumJson.description() != null ? museumJson.description() : "")
                .setPhoto(museumJson.photo() != null ? museumJson.photo() : "")
                .setGeo(Geo.newBuilder()
                        .setCity(museumJson.geo().city() != null ? museumJson.geo().city() : "")
                        .setCountry(Country.newBuilder()
                                .setId(museumJson.geo().country().id().toString())
                                .setName(museumJson.geo().country().name() != null ? museumJson.geo().country().name() : "")
                                .build())
                ).build();

        Museum response = museumServiceStub.createMuseum(request);
        return fromGrpcMuseum(response);
    }

    @Override
    public MuseumJson updateMuseum(MuseumJson museumJson) {
        UpdateMuseumRequest.Builder requestBuilder = UpdateMuseumRequest.newBuilder()
                .setId(museumJson.id().toString())
                .setTitle(museumJson.title())
                .setDescription(museumJson.description() != null ? museumJson.description() : "");

        if (museumJson.photo() != null) {
            requestBuilder.setPhoto(museumJson.photo());
        }

        if (museumJson.geo() != null) {
            Geo.Builder geoBuilder = Geo.newBuilder();
            if (museumJson.geo().city() != null) {
                geoBuilder.setCity(museumJson.geo().city());
            }
            if (museumJson.geo().country() != null) {
                Country country = Country.newBuilder()
                        .setId(museumJson.geo().country().id().toString())
                        .setName(museumJson.geo().country().name() != null ? museumJson.geo().country().name() : "")
                        .build();
                geoBuilder.setCountry(country);
            }
            requestBuilder.setGeo(geoBuilder.build());
        }

        Museum museum = museumServiceStub.updateMuseum(requestBuilder.build());
        return fromGrpcMuseum(museum);
    }
}
