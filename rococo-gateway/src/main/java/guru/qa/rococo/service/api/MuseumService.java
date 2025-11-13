package guru.qa.rococo.service.api;

import guru.qa.rococo.model.MuseumJson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MuseumService {

    Page<MuseumJson> getAllMuseums(Pageable pageable, String title);

    MuseumJson getMuseumById(UUID id);

    MuseumJson createMuseum(MuseumJson museumJson);

    MuseumJson updateMuseum(MuseumJson museumJson);
}
