package guru.qa.rococo.data.entity.museum;

import guru.qa.rococo.utils.RandomDataUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

import static guru.qa.rococo.utils.ResourceUtils.imageToDataUrl;

@Getter
@Setter
@Entity
@Table(name = "museum")
public class MuseumEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "city")
    private String city;

    @Column(name = "photo")
    private byte[] photo;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "country_id", nullable = false, referencedColumnName = "id")
    private CountryEntity country;

    public static MuseumEntity createMuseum(CountryEntity country) {
        MuseumEntity museum = new MuseumEntity();
        museum.setId(null);
        museum.setTitle(RandomDataUtils.randomMuseumName());
        museum.setDescription(RandomDataUtils.randomSentence(15));
        museum.setCity(RandomDataUtils.randomCityName());
        museum.setPhoto(imageToDataUrl("img/museum.jpg").getBytes());
        museum.setCountry(country);

        return museum;
    }
}