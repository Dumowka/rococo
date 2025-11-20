package guru.qa.rococo.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.ThreadLocalRandom;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Countries {
    RUSSIA("Россия"),
    UAS("США"),
    FRANCE("Франция"),
    POLAND("Польша"),
    ARMENIA("Армения");

    private String name;

    public static Countries random() {
        Countries[] values = Countries.values();
        int index = ThreadLocalRandom.current().nextInt(values.length);
        return values[index];
    }
}
