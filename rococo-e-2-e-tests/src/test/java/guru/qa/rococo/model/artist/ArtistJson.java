package guru.qa.rococo.model.artist;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.rococo.data.entity.artist.ArtistEntity;
import guru.qa.rococo.grpc.Artist;
import guru.qa.rococo.model.PhotoPaths;
import guru.qa.rococo.utils.RandomDataUtils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static guru.qa.rococo.utils.ResourceUtils.imageToDataUrl;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ArtistJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("name")
        String name,
        @JsonProperty("biography")
        String biography,
        @JsonProperty("photo")
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

        public static ArtistJson fromEntity(ArtistEntity entity) {
                return new ArtistJson(
                        entity.getId(),
                        entity.getName(),
                        entity.getBiography(),
                        entity.getPhoto() != null && entity.getPhoto().length > 0 ? new String(entity.getPhoto(), StandardCharsets.UTF_8) : null
                );
        }

        public static ArtistJson createArtist() {
                return new ArtistJson(
                        null,
                        RandomDataUtils.randomArtistName(),
                        RandomDataUtils.randomArtistBiography(),
                        imageToDataUrl(PhotoPaths.ARTIST)
                );
        }
}
