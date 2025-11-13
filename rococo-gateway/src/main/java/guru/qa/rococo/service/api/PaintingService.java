package guru.qa.rococo.service.api;

import guru.qa.rococo.model.PaintingJson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PaintingService {

    Page<PaintingJson> getAllPainting(Pageable pageable, String title);

    PaintingJson getPaintingById(UUID id);

    Page<PaintingJson> getPaintingsByArtistId(UUID artistId, Pageable pageable);

    PaintingJson createPainting(PaintingJson paintingJson);

    PaintingJson updatePainting(PaintingJson paintingJson);
}
