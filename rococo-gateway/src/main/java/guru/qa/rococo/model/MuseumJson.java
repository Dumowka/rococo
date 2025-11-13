package guru.qa.rococo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.rococo.grpc.Museum;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MuseumJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("title")
        @Size(max = 50, message = "Title name can`t be longer than 50 characters")
        String title,
        @JsonProperty("description")
        @Size(max = 1000, message = "Description can`t be longer than 1000 characters")
        String description,
        @JsonProperty("photo")
        @Size(max = 1024 * 1024 * 15, message = "Photo can`t be bigger than 15MB")
        String photo,
        @JsonProperty("geo")
        Geo geo
) {
    public record Geo(
            @JsonProperty("city")
            @Size(max = 255, message = "City name can`t be longer than 255 characters")
            String city,
            @JsonProperty("country")
            CountryJson country
    ) {
        public static Geo fromGrpcGeo(guru.qa.rococo.grpc.Geo geo) {
            CountryJson country = null;
            country = new CountryJson(
                    UUID.fromString(geo.getCountry().getId()),
                    geo.getCountry().getName()
            );
            return new MuseumJson.Geo(
                    geo.getCity().isEmpty() ? null : geo.getCity(),
                    country
            );
        }
    }

    public static MuseumJson fromGrpcMuseum(Museum museum) {
        MuseumJson.Geo geo = museum.hasGeo()
                ? Geo.fromGrpcGeo(museum.getGeo())
                : null;

        return new MuseumJson(
                UUID.fromString(museum.getId()),
                museum.getTitle(),
                museum.getDescription().isEmpty() ? null : museum.getDescription(),
                museum.getPhoto().isEmpty() ? null : museum.getPhoto(),
                geo
        );
    }

}
