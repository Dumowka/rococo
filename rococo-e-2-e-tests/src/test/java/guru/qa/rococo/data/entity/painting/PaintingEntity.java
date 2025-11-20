package guru.qa.rococo.data.entity.painting;

import guru.qa.rococo.utils.RandomDataUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

import static guru.qa.rococo.utils.ResourceUtils.imageToDataUrl;

@Getter
@Setter
@Entity
@Table(name = "painting")
public class PaintingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "artist_id", nullable = false)
    private UUID artistId;

    @Column(name = "museum_id")
    private UUID museumId;

    @Column(name = "content")
    private byte[] content;

    public static PaintingEntity createPainting(UUID museumId, UUID artistId) {
        PaintingEntity painting = new PaintingEntity();
        painting.setId(null);
        painting.setTitle(RandomDataUtils.randomPaintingName());
        painting.setDescription(RandomDataUtils.randomSentence(15));
        painting.setContent(imageToDataUrl("img/painting.jpeg").getBytes());
        painting.setMuseumId(museumId);
        painting.setArtistId(artistId);

        return painting;
    }
}