package guru.qa.rococo.jupiter.annotation;

import guru.qa.rococo.jupiter.extension.MuseumExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith(MuseumExtension.class)
public @interface Museum {
    String title() default "";
    String country() default "Россия";
    String city() default "Барнаул";
    String description() default "Описание города по-умолчанию";
    String imagePath() default "img/museum.jpg";
}
