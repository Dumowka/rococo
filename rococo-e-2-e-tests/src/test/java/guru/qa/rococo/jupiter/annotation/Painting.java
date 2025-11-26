package guru.qa.rococo.jupiter.annotation;

import guru.qa.rococo.model.PhotoPaths;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Painting {
    String title() default "";
    String description() default "Описание картины по-умолчанию";
    String content() default PhotoPaths.PAINTING;
    Artist artist() default @Artist();
    Museum museum() default @Museum();
}
