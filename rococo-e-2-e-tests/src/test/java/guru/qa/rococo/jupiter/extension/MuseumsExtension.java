package guru.qa.rococo.jupiter.extension;

import guru.qa.rococo.data.entity.museum.CountryEntity;
import guru.qa.rococo.data.entity.museum.MuseumEntity;
import guru.qa.rococo.jupiter.annotation.Museum;
import guru.qa.rococo.jupiter.annotation.Museums;
import guru.qa.rococo.model.Countries;
import guru.qa.rococo.model.museum.MuseumJson;
import guru.qa.rococo.service.impl.CountryDbClient;
import guru.qa.rococo.service.impl.MuseumDbClient;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.ArrayList;
import java.util.List;

public class MuseumsExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(MuseumsExtension.class);

    private final MuseumDbClient museumDbClient = new MuseumDbClient();
    private final CountryDbClient countryDbClient = new CountryDbClient();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Museums.class)
                .ifPresent(museumAnno -> {
                    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Museum.class).ifPresent(museum -> {
                        throw new IllegalStateException("@Museums не должна использоваться вместе с @Museum");
                    });

                    if (museumAnno.count() <= 0) {
                        return;
                    }
                    final List<MuseumJson> listMuseums = new ArrayList<>();
                    for (int i = 0; i < museumAnno.count(); i++) {
                        CountryEntity country = countryDbClient.findByName(Countries.random().getName());
                        MuseumJson museum = museumDbClient.createMuseum(MuseumEntity.createMuseum(country));
                        listMuseums.add(museum);
                    }
                    setMuseums(listMuseums);
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(List.class) &&
                parameterContext.getParameter().getParameterizedType().toString().contains("MuseumJson");
    }

    @Override
    public List<MuseumJson> resolveParameter(ParameterContext parameterContext,
                                             ExtensionContext extensionContext) throws ParameterResolutionException {
        return getMuseums();
    }

    public static List<MuseumJson> getMuseums() {
        final ExtensionContext context = TestMethodContextExtension.context();
        return context.getStore(NAMESPACE).get(
                context.getUniqueId(), List.class
        );
    }

    public static void setMuseums(List<MuseumJson> museums) {
        final ExtensionContext context = TestMethodContextExtension.context();
        context.getStore(NAMESPACE).put(
                context.getUniqueId(),
                museums
        );
    }
}
