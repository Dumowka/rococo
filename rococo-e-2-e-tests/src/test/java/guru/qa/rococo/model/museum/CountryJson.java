package guru.qa.rococo.model.museum;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.rococo.data.entity.museum.CountryEntity;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CountryJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("name")
        String name
) {
        public static CountryJson fromEntity(CountryEntity entity) {
                return new CountryJson(
                        entity.getId(),
                        entity.getName()
                );
        }
}
