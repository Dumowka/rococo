package guru.qa.rococo.jupiter.extension;

import guru.qa.rococo.data.entity.artist.ArtistEntity;
import guru.qa.rococo.jupiter.annotation.Artist;
import guru.qa.rococo.jupiter.annotation.Artists;
import guru.qa.rococo.model.artist.ArtistJson;
import guru.qa.rococo.service.impl.ArtistDbClient;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.ArrayList;
import java.util.List;

public class ArtistsExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(ArtistsExtension.class);

    private final ArtistDbClient artistDbClient = new ArtistDbClient();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Artists.class)
                .ifPresent(artistsAnno -> {
                    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Artist.class).ifPresent(artist -> {
                        throw new IllegalStateException("@Artists не должна использоваться вместе с @Artist");
                    });

                    if (artistsAnno.count() <= 0) {
                        return;
                    }
                    List<ArtistJson> artists = new ArrayList<>();
                    for (int i = 0; i < artistsAnno.count(); i++) {
                        ArtistJson artist = artistDbClient.createArtist(ArtistEntity.createArtist());
                        artists.add(artist);
                    }
                    setArtists(artists);
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(List.class) &&
                parameterContext.getParameter().getParameterizedType().toString().contains("ArtistJson");
    }

    @Override
    public List<ArtistJson> resolveParameter(ParameterContext parameterContext,
                                             ExtensionContext extensionContext) throws ParameterResolutionException {
        return getArtists();
    }

    public static List<ArtistJson> getArtists() {
        final ExtensionContext context = TestMethodContextExtension.context();
        return context.getStore(NAMESPACE).get(
                context.getUniqueId(), List.class
        );
    }

    public static void setArtists(List<ArtistJson> artists) {
        final ExtensionContext context = TestMethodContextExtension.context();
        context.getStore(NAMESPACE).put(
                context.getUniqueId(),
                artists
        );
    }
}
