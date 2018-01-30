package io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.TestUtils.splitString;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class BinaryReaderTest {


  private final BinaryReader binaryReader = new BinaryReader();

  static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
        {"11100101", "11100101"},
        {"11100101a", "Input contains non binary characters: \"11100101a\""},
        {"", "Input is empty"},
        {"Lel, not binary at all", "Input contains non binary characters: \"Lel, not binary at all\""},
        {"0101 01", "Input contains non binary characters: \"0101 01\""},
    });
  }


  @DisplayName("Binary Input Test")
  @ParameterizedTest(name = "Parsed \"{0}\", expected \"{1}\"")
  @MethodSource(value = "data")
  public void testBinaryInput(String candidateString, String expected) {
    try {
      List<Integer> binaryList = binaryReader.readString(candidateString);
      assertEquals(splitString(expected), binaryList);
    }
    catch (IllegalArgumentException e) {
      assertEquals(expected, e.getMessage());
    }
  }
}
