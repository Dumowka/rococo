package guru.qa.rococo.model.painting;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.rococo.data.entity.painting.PaintingEntity;
import guru.qa.rococo.grpc.Painting;
import guru.qa.rococo.model.artist.ArtistJson;
import guru.qa.rococo.model.museum.MuseumJson;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PaintingJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("title")
        String title,
        @JsonProperty("description")
        String description,
        @JsonProperty("content")
        String content,
        @JsonProperty("museum")
        MuseumJson museum,
        @JsonProperty("artist")
        ArtistJson artist
) {
        public static PaintingJson fromGrpcPainting(Painting painting, MuseumJson museum, ArtistJson artist) {
                return new PaintingJson(
                        UUID.fromString(painting.getId()),
                        painting.getTitle(),
                        painting.getDescription().isEmpty() ? null : painting.getDescription(),
                        painting.getContent().isEmpty() ? null : painting.getContent(),
                        museum,
                        artist
                );
        }

        public PaintingJson addMuseumAndArtist(MuseumJson museum, ArtistJson artist) {
                return new PaintingJson(
                        id,
                        title,
                        description,
                        content,
                        museum,
                        artist
                );
        }

        public static PaintingJson fromEntity(PaintingEntity entity) {
                return fromEntity(entity, null, null);
        }

        public static PaintingJson fromEntity(PaintingEntity entity, MuseumJson museum, ArtistJson artist) {
                return new PaintingJson(
                        entity.getId(),
                        entity.getTitle(),
                        entity.getDescription(),
                        entity.getContent() != null && entity.getContent().length > 0 ? new String(entity.getContent(), StandardCharsets.UTF_8) : null,
                        museum,
                        artist
                );
        }
}
