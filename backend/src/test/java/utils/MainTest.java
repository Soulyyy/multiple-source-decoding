package utils;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTest {

    @Test
    public void testSayHello() {
        assertEquals(Main.sayHello(), "Hello, World!");
    }
}
