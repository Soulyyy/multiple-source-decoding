package utils;

import java.util.Arrays;

public class MathUtils {


  public static Integer addMod(Integer mod, Integer... integers) {
    return Arrays.stream(integers).reduce((a, b) -> a + b).orElse(0) % mod;
  }
}
