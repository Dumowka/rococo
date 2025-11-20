package guru.qa.rococo.jupiter.extension;

import guru.qa.rococo.data.entity.artist.ArtistEntity;
import guru.qa.rococo.data.entity.museum.CountryEntity;
import guru.qa.rococo.data.entity.museum.MuseumEntity;
import guru.qa.rococo.data.entity.painting.PaintingEntity;
import guru.qa.rococo.jupiter.annotation.Painting;
import guru.qa.rococo.jupiter.annotation.Paintings;
import guru.qa.rococo.model.Countries;
import guru.qa.rococo.model.artist.ArtistJson;
import guru.qa.rococo.model.museum.MuseumJson;
import guru.qa.rococo.model.painting.PaintingJson;
import guru.qa.rococo.service.impl.ArtistDbClient;
import guru.qa.rococo.service.impl.CountryDbClient;
import guru.qa.rococo.service.impl.MuseumDbClient;
import guru.qa.rococo.service.impl.PaintingDbClient;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.ArrayList;
import java.util.List;

public class PaintingsExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(PaintingsExtension.class);

    private final PaintingDbClient paintingDbClient = new PaintingDbClient();
    private final MuseumDbClient museumDbClient = new MuseumDbClient();
    private final CountryDbClient countryDbClient = new CountryDbClient();
    private final ArtistDbClient artistDbClient = new ArtistDbClient();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Paintings.class)
                .ifPresent(paintingAnno -> {
                    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Painting.class).ifPresent(painting -> {
                        throw new IllegalStateException("@Paintings не должна использоваться вместе с @Painting");
                    });

                    if (paintingAnno.count() <= 0) {
                        return;
                    }
                    final List<PaintingJson> listPaintings = new ArrayList<>();
                    for (int i = 0; i < paintingAnno.count(); i++) {
                        CountryEntity country = countryDbClient.findByName(Countries.random().getName());
                        MuseumJson museum = museumDbClient.createMuseum(MuseumEntity.createMuseum(country));
                        ArtistJson artist = artistDbClient.createArtist(ArtistEntity.createArtist());
                        PaintingJson painting = paintingDbClient.createPainting(PaintingEntity.createPainting(museum.id(), artist.id()));

                        listPaintings.add(painting.addMuseumAndArtist(museum, artist));
                    }
                    setPaintings(listPaintings);
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(List.class) &&
                parameterContext.getParameter().getParameterizedType().toString().contains("PaintingJson");
    }

    @Override
    public List<PaintingJson> resolveParameter(ParameterContext parameterContext,
                                               ExtensionContext extensionContext) throws ParameterResolutionException {
        return getPaintings();
    }

    public static List<PaintingJson> getPaintings() {
        final ExtensionContext context = TestMethodContextExtension.context();
        return context.getStore(NAMESPACE).get(
                context.getUniqueId(), List.class
        );
    }

    public static void setPaintings(List<PaintingJson> paintings) {
        final ExtensionContext context = TestMethodContextExtension.context();
        context.getStore(NAMESPACE).put(
                context.getUniqueId(),
                paintings
        );
    }
}
