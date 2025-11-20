package guru.qa.rococo.model.museum;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.rococo.data.entity.museum.MuseumEntity;
import guru.qa.rococo.grpc.Museum;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MuseumJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("title")
        String title,
        @JsonProperty("description")
        String description,
        @JsonProperty("photo")
        String photo,
        @JsonProperty("geo")
        Geo geo
) {
    public record Geo(
            @JsonProperty("city")
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
            return new Geo(
                    geo.getCity().isEmpty() ? null : geo.getCity(),
                    country
            );
        }
    }

    public static MuseumJson fromGrpcMuseum(Museum museum) {
        Geo geo = museum.hasGeo()
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

    public static MuseumJson fromEntity(MuseumEntity entity) {
        return new MuseumJson(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getPhoto() != null && entity.getPhoto().length > 0 ? new String(entity.getPhoto(), StandardCharsets.UTF_8) : null,
                new Geo(
                        entity.getCity(),
                        CountryJson.fromEntity(entity.getCountry())
                )
        );
    }

}
