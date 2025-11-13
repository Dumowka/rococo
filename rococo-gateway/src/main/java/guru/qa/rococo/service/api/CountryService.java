package guru.qa.rococo.service.api;

import guru.qa.rococo.model.CountryJson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CountryService {

    Page<CountryJson> getAllCountries(Pageable pageable);

}
