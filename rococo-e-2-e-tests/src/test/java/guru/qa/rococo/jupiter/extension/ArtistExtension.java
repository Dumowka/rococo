package guru.qa.rococo.jupiter.extension;

import guru.qa.rococo.data.entity.artist.ArtistEntity;
import guru.qa.rococo.jupiter.annotation.Artist;
import guru.qa.rococo.model.artist.ArtistJson;
import guru.qa.rococo.service.impl.ArtistDbClient;
import guru.qa.rococo.utils.RandomDataUtils;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import static guru.qa.rococo.utils.ResourceUtils.imageToDataUrl;

public class ArtistExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(ArtistExtension.class);

    private final ArtistDbClient artistDbClient = new ArtistDbClient();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Artist.class)
                .ifPresent(artistAnno -> {
                    ArtistEntity artist = new ArtistEntity();
                    artist.setId(null);
                    artist.setName("".equals(artistAnno.name()) ? RandomDataUtils.randomArtistName() : artistAnno.name());
                    artist.setBiography(artistAnno.biography());
                    artist.setPhoto(imageToDataUrl(artistAnno.photo()).getBytes());

                    setArtist(artistDbClient.createArtist(artist));
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(ArtistJson.class);
    }

    @Override
    public ArtistJson resolveParameter(ParameterContext parameterContext,
                                       ExtensionContext extensionContext) throws ParameterResolutionException {
        return getArtist();
    }

    public static ArtistJson getArtist() {
        final ExtensionContext context = TestMethodContextExtension.context();
        return context.getStore(NAMESPACE).get(
                context.getUniqueId(), ArtistJson.class
        );
    }

    public static void setArtist(ArtistJson artist) {
        final ExtensionContext context = TestMethodContextExtension.context();
        context.getStore(NAMESPACE).put(
                context.getUniqueId(),
                artist
        );
    }
}
