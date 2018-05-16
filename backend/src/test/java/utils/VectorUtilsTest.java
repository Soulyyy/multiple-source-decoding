package utils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class VectorUtilsTest {

  @Test
  void testGetBinaryVector() {
    List<Integer> binaryVector = VectorUtils.getBinaryVector(0);
    assertEquals(Collections.singletonList(0), binaryVector);

    binaryVector = VectorUtils.getBinaryVector(1);
    assertEquals(Collections.singletonList(1), binaryVector);

    binaryVector = VectorUtils.getBinaryVector(2);
    assertEquals(Arrays.asList(1, 0), binaryVector);

    binaryVector = VectorUtils.getBinaryVector(43);
    assertEquals(Arrays.asList(1, 0, 1, 0, 1, 1), binaryVector);

    binaryVector = VectorUtils.getBinaryVector(678);
    assertEquals(Arrays.asList(1, 0, 1, 0, 1, 0, 0, 1, 1, 0), binaryVector);
  }
}