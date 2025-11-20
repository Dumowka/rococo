package guru.qa.rococo.jupiter.extension;

import guru.qa.rococo.data.entity.museum.MuseumEntity;
import guru.qa.rococo.jupiter.annotation.Museum;
import guru.qa.rococo.model.museum.MuseumJson;
import guru.qa.rococo.service.impl.CountryDbClient;
import guru.qa.rococo.service.impl.MuseumDbClient;
import guru.qa.rococo.utils.RandomDataUtils;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import static guru.qa.rococo.utils.ResourceUtils.imageToDataUrl;

public class MuseumExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(MuseumExtension.class);

    private final MuseumDbClient museumDbClient = new MuseumDbClient();
    private final CountryDbClient countryDbClient = new CountryDbClient();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Museum.class)
                .ifPresent(museumAnno -> {
                    MuseumEntity museum = new MuseumEntity();
                    museum.setId(null);
                    museum.setTitle("".equals(museumAnno.title()) ? RandomDataUtils.randomMuseumName() : museumAnno.title());
                    museum.setDescription(museumAnno.description());
                    museum.setCity(museumAnno.city());
                    museum.setPhoto(imageToDataUrl(museumAnno.imagePath()).getBytes());
                    museum.setCountry(
                            countryDbClient.findByName(museumAnno.country())
                    );

                    setMuseum(museumDbClient.createMuseum(museum));
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(MuseumJson.class);
    }

    @Override
    public MuseumJson resolveParameter(ParameterContext parameterContext,
                                       ExtensionContext extensionContext) throws ParameterResolutionException {
        return getMuseum();
    }

    public static MuseumJson getMuseum() {
        final ExtensionContext context = TestMethodContextExtension.context();
        return context.getStore(NAMESPACE).get(
                context.getUniqueId(), MuseumJson.class
        );
    }

    public static void setMuseum(MuseumJson museum) {
        final ExtensionContext context = TestMethodContextExtension.context();
        context.getStore(NAMESPACE).put(
                context.getUniqueId(),
                museum
        );
    }
}
