package guru.qa.rococo.service;

import guru.qa.rococo.data.CountryEntity;
import guru.qa.rococo.data.MuseumEntity;
import guru.qa.rococo.data.repository.CountryRepository;
import guru.qa.rococo.data.repository.MuseumRepository;
import guru.qa.rococo.ex.CountryNotFoundException;
import guru.qa.rococo.ex.MuseumNotFoundException;
import guru.qa.rococo.grpc.Artist;
import guru.qa.rococo.grpc.Country;
import guru.qa.rococo.grpc.CreateMuseumRequest;
import guru.qa.rococo.grpc.Geo;
import guru.qa.rococo.grpc.GetAllMuseumsRequest;
import guru.qa.rococo.grpc.GetAllMuseumsResponse;
import guru.qa.rococo.grpc.GetMuseumByIdRequest;
import guru.qa.rococo.grpc.Museum;
import guru.qa.rococo.grpc.RococoMuseumServiceGrpc;
import guru.qa.rococo.grpc.UpdateMuseumRequest;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.grpc.server.service.GrpcService;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@GrpcService
public class MuseumGrpcService extends RococoMuseumServiceGrpc.RococoMuseumServiceImplBase {

    private final MuseumRepository museumRepository;
    private final CountryRepository countryRepository;

    @Autowired
    public MuseumGrpcService(MuseumRepository museumRepository, CountryRepository countryRepository) {
        this.museumRepository = museumRepository;
        this.countryRepository = countryRepository;
    }

    @Override
    public void getAllMuseums(GetAllMuseumsRequest request, StreamObserver<GetAllMuseumsResponse> responseObserver) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<MuseumEntity> museums = request.hasTitle() && !request.getTitle().isEmpty()
                ? museumRepository.findByTitleContainingIgnoreCase(request.getTitle(), pageable)
                : museumRepository.findAll(pageable);

        GetAllMuseumsResponse.Builder responseBuilder = GetAllMuseumsResponse.newBuilder()
                .setTotalPages(museums.getTotalPages())
                .setTotalElements(museums.getTotalElements())
                .setCurrentPage(museums.getNumber())
                .setPageSize(museums.getSize());

        for (MuseumEntity museum : museums) {
            responseBuilder.addMuseums(convertToGrpcArtist(museum));
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getMuseumById(GetMuseumByIdRequest request, StreamObserver<Museum> responseObserver) {
        UUID id = UUID.fromString(request.getId());
        MuseumEntity museum = museumRepository.findById(id)
                .orElseThrow(MuseumNotFoundException::new);

        responseObserver.onNext(convertToGrpcArtist(museum));
        responseObserver.onCompleted();
    }

    @Override
    public void createMuseum(CreateMuseumRequest request, StreamObserver<Museum> responseObserver) {
        UUID countryId = UUID.fromString(request.getGeo().getCountry().getId());
        CountryEntity countryEntity = countryRepository.findById(countryId)
                .orElseThrow(CountryNotFoundException::new);

        MuseumEntity museumEntity = new MuseumEntity();
        museumEntity.setTitle(request.getTitle());
        museumEntity.setDescription(request.getDescription());
        if (!request.getPhoto().isEmpty()) {
            museumEntity.setPhoto(request.getPhoto().getBytes(StandardCharsets.UTF_8));
        }
        museumEntity.setCity(request.getGeo().getCity());
        museumEntity.setCountry(countryEntity);

        MuseumEntity savedMuseum = museumRepository.save(museumEntity);
        responseObserver.onNext(convertToGrpcArtist(savedMuseum));
        responseObserver.onCompleted();
    }

    @Override
    public void updateMuseum(UpdateMuseumRequest request, StreamObserver<Museum> responseObserver) {
        UUID id = UUID.fromString(request.getId());
        MuseumEntity museumEntity = museumRepository.findById(id)
                .orElseThrow(MuseumNotFoundException::new);

        museumEntity.setTitle(request.getTitle());
        museumEntity.setDescription(request.getDescription());
        museumEntity.setPhoto(request.getPhoto().getBytes(StandardCharsets.UTF_8));
        museumEntity.setCity(request.getGeo().getCity());

        UUID countryId = UUID.fromString(request.getGeo().getCountry().getId());
        CountryEntity countryEntity = countryRepository.findById(countryId)
                .orElseThrow(CountryNotFoundException::new);
        museumEntity.setCountry(countryEntity);

        MuseumEntity savedMuseum = museumRepository.save(museumEntity);
        responseObserver.onNext(convertToGrpcArtist(savedMuseum));
        responseObserver.onCompleted();
    }

    private Museum convertToGrpcArtist(MuseumEntity entity) {
        Museum.Builder builder = Museum.newBuilder()
                .setId(entity.getId().toString())
                .setTitle(entity.getTitle())
                .setDescription(entity.getDescription() != null ? entity.getDescription() : "");

        if (entity.getPhoto() != null && entity.getPhoto().length > 0) {
            builder.setPhoto(new String(entity.getPhoto(), StandardCharsets.UTF_8));
        }

        if (entity.getCity() != null || entity.getCountry() != null) {
            Geo.Builder geoBuilder = Geo.newBuilder();
            if (entity.getCity() != null) {
                geoBuilder.setCity(entity.getCity());
            }
            if (entity.getCountry() != null) {
                Country country = Country.newBuilder()
                        .setId(entity.getCountry().getId().toString())
                        .setName(entity.getCountry().getName())
                        .build();
                geoBuilder.setCountry(country);
            }
            builder.setGeo(geoBuilder.build());
        }

        return builder.build();
    }
}
