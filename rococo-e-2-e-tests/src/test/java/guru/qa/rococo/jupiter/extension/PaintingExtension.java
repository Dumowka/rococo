package guru.qa.rococo.jupiter.extension;

import guru.qa.rococo.data.entity.artist.ArtistEntity;
import guru.qa.rococo.data.entity.museum.MuseumEntity;
import guru.qa.rococo.data.entity.painting.PaintingEntity;
import guru.qa.rococo.jupiter.annotation.Artist;
import guru.qa.rococo.jupiter.annotation.Museum;
import guru.qa.rococo.jupiter.annotation.Painting;
import guru.qa.rococo.model.artist.ArtistJson;
import guru.qa.rococo.model.museum.MuseumJson;
import guru.qa.rococo.model.painting.PaintingJson;
import guru.qa.rococo.service.impl.ArtistDbClient;
import guru.qa.rococo.service.impl.CountryDbClient;
import guru.qa.rococo.service.impl.MuseumDbClient;
import guru.qa.rococo.service.impl.PaintingDbClient;
import guru.qa.rococo.utils.RandomDataUtils;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import javax.annotation.Nonnull;

import static guru.qa.rococo.utils.ResourceUtils.imageToDataUrl;

public class PaintingExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(PaintingExtension.class);

    private final PaintingDbClient paintingDbClient = new PaintingDbClient();
    private final MuseumDbClient museumDbClient = new MuseumDbClient();
    private final CountryDbClient countryDbClient = new CountryDbClient();
    private final ArtistDbClient artistDbClient = new ArtistDbClient();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Painting.class)
                .ifPresent(paintingAnno -> {
                    MuseumJson museum = createMuseum(paintingAnno.museum());
                    ArtistJson artist = createArtist(paintingAnno.artist());

                    PaintingEntity painting = new PaintingEntity();
                    painting.setId(null);
                    painting.setTitle("".equals(paintingAnno.title()) ? RandomDataUtils.randomPaintingName() : paintingAnno.title());
                    painting.setDescription(paintingAnno.description());
                    painting.setContent(imageToDataUrl(paintingAnno.content()).getBytes());
                    painting.setMuseumId(museum.id());
                    painting.setArtistId(artist.id());

                    PaintingJson createdPainting = paintingDbClient.createPainting(painting);
                    setPainting(createdPainting.addMuseumAndArtist(museum, artist));
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(PaintingJson.class);
    }

    @Override
    public PaintingJson resolveParameter(ParameterContext parameterContext,
                                         ExtensionContext extensionContext) throws ParameterResolutionException {
        return getPainting();
    }

    @Nonnull
    private MuseumJson createMuseum(Museum museumAnno) {
        MuseumEntity museum = new MuseumEntity();
        museum.setId(null);
        museum.setTitle("".equals(museumAnno.title()) ? RandomDataUtils.randomMuseumName() : museumAnno.title());
        museum.setDescription(museumAnno.description());
        museum.setCity(museumAnno.city());
        museum.setPhoto(imageToDataUrl(museumAnno.imagePath()).getBytes());
        museum.setCountry(
                countryDbClient.findByName(museumAnno.country())
        );

        return museumDbClient.createMuseum(museum);
    }

    @Nonnull
    private ArtistJson createArtist(Artist artistAnno) {
        ArtistEntity artist = new ArtistEntity();
        artist.setId(null);
        artist.setName("".equals(artistAnno.name()) ? RandomDataUtils.randomArtistName() : artistAnno.name());
        artist.setBiography(artistAnno.biography());
        artist.setPhoto(imageToDataUrl(artistAnno.photo()).getBytes());

        return artistDbClient.createArtist(artist);
    }

    public static PaintingJson getPainting() {
        final ExtensionContext context = TestMethodContextExtension.context();
        return context.getStore(NAMESPACE).get(
                context.getUniqueId(), PaintingJson.class
        );
    }

    public static void setPainting(PaintingJson painting) {
        final ExtensionContext context = TestMethodContextExtension.context();
        context.getStore(NAMESPACE).put(
                context.getUniqueId(),
                painting
        );
    }
}
