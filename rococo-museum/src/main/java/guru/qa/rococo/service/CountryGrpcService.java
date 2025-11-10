package guru.qa.rococo.service;

import guru.qa.rococo.data.CountryEntity;
import guru.qa.rococo.data.MuseumEntity;
import guru.qa.rococo.data.repository.CountryRepository;
import guru.qa.rococo.grpc.Country;
import guru.qa.rococo.grpc.GetAllCountriesRequest;
import guru.qa.rococo.grpc.GetAllCountriesResponse;
import guru.qa.rococo.grpc.RococoCountryServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
public class CountryGrpcService extends RococoCountryServiceGrpc.RococoCountryServiceImplBase {

    private final CountryRepository countryRepository;

    @Autowired
    public CountryGrpcService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public void getAllCountries(GetAllCountriesRequest request, StreamObserver<GetAllCountriesResponse> responseObserver) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<CountryEntity> countries = countryRepository.findAll(pageable);

        GetAllCountriesResponse.Builder responseBuilder = GetAllCountriesResponse.newBuilder();
        responseBuilder.setTotalPages(countries.getTotalPages())
                .setTotalElements(countries.getTotalElements())
                .setCurrentPage(countries.getNumber())
                .setPageSize(countries.getSize());

        for (CountryEntity country : countries.getContent()) {
            responseBuilder.addCountries(convertToGrpcCountry(country));
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    private Country convertToGrpcCountry(CountryEntity entity) {
        return Country.newBuilder()
                .setId(entity.getId().toString())
                .setName(entity.getName())
                .build();
    }
}
