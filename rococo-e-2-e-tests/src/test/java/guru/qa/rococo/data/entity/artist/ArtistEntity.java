package guru.qa.rococo.data.entity.artist;

import guru.qa.rococo.model.PhotoPaths;
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
@Table(name = "artist")
public class ArtistEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "biography", nullable = false, length = 2000)
    private String biography;

    @Column(name = "photo")
    private byte[] photo;

    public static ArtistEntity createArtist() {
        ArtistEntity artist = new ArtistEntity();
        artist.setId(null);
        artist.setName(RandomDataUtils.randomArtistName());
        artist.setBiography(RandomDataUtils.randomArtistBiography());
        artist.setPhoto(imageToDataUrl(PhotoPaths.ARTIST).getBytes());

        return artist;
    }
}