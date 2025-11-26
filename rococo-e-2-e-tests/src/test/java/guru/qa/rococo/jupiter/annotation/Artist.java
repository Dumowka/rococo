package guru.qa.rococo.jupiter.annotation;

import guru.qa.rococo.model.PhotoPaths;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Artist {
    String name() default "";
    String biography() default "Описание художника по-умолчанию";
    String photo() default PhotoPaths.ARTIST;
}
