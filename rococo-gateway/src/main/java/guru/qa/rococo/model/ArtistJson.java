package guru.qa.rococo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.rococo.grpc.Artist;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ArtistJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("name")
        @Size(max = 255, message = "Name can`t be longer than 255 characters")
        String name,
        @JsonProperty("biography")
        @Size(max = 1000, message = "Biography can`t be longer than 1000 characters")
        String biography,
        @JsonProperty("photo")
        @Size(max = 1024 * 1024 * 15, message = "Photo can`t be bigger than 15MB")
        String photo
) {
        public static ArtistJson fromGrpcArtist(Artist artist) {
                return new ArtistJson(
                        UUID.fromString(artist.getId()),
                        artist.getName(),
                        artist.getBiography().isEmpty() ? null : artist.getBiography(),
                        artist.getPhoto().isEmpty() ? null : artist.getPhoto()
                );
        }
}
