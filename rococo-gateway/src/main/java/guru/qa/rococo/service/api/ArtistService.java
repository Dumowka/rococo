package guru.qa.rococo.service.api;

import guru.qa.rococo.model.ArtistJson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ArtistService {

    Page<ArtistJson> getAllArtist(Pageable pageable, String name);

    ArtistJson getArtistById(UUID id);

    ArtistJson createArtist(ArtistJson artistJson);

    ArtistJson updateArtist(ArtistJson artistJson);

}
