package guru.qa.rococo.test;

import guru.qa.rococo.jupiter.annotation.Paintings;
import org.junit.jupiter.api.Test;

public class DbTests {

    @Paintings(count = 2)
    @Test
    public void test() {
        System.out.println();
    }
}
