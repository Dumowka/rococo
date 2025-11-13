package guru.qa.rococo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.rococo.grpc.Country;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CountryJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("name")
        @Size(max = 255, message = "Country name can`t be longer than 255 characters")
        String name
) {
        public static CountryJson fromGrpcCountry(Country country) {
                return new CountryJson(
                        UUID.fromString(country.getId()),
                        country.getName()
                );
        }
}
