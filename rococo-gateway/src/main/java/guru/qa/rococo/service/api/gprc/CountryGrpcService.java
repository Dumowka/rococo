package guru.qa.rococo.service.api.gprc;

import guru.qa.rococo.grpc.GetAllCountriesRequest;
import guru.qa.rococo.grpc.GetAllCountriesResponse;
import guru.qa.rococo.grpc.RococoCountryServiceGrpc;
import guru.qa.rococo.model.CountryJson;
import guru.qa.rococo.service.api.CountryService;
import io.grpc.StatusRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryGrpcService implements CountryService {

    private final RococoCountryServiceGrpc.RococoCountryServiceBlockingStub countryServiceStub;

    @Autowired
    public CountryGrpcService(RococoCountryServiceGrpc.RococoCountryServiceBlockingStub countryServiceStub) {
        this.countryServiceStub = countryServiceStub;
    }

    @Override
    public Page<CountryJson> getAllCountries(Pageable pageable) {
        try {
            GetAllCountriesRequest request = GetAllCountriesRequest.newBuilder()
                    .setPage(pageable.getPageNumber())
                    .setSize(pageable.getPageSize())
                    .build();

            GetAllCountriesResponse response = countryServiceStub.getAllCountries(request);

            List<CountryJson> countries = response.getCountriesList().stream()
                    .map(CountryJson::fromGrpcCountry)
                    .toList();

            Pageable grpcPageable = PageRequest.of(
                    response.getCurrentPage(),
                    response.getPageSize()
            );

            return new PageImpl<>(
                    countries,
                    grpcPageable,
                    response.getTotalElements()
            );
        } catch (StatusRuntimeException e) {
            throw new RuntimeException("Error get all countries: " + e.getStatus().getDescription(), e);
        }
    }
}
