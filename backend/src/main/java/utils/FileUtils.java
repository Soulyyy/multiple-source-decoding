package utils;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileUtils {

  public static Stream<String> getResourceAsStream(String name) {
    try {
      URL path = FileUtils.class.getClassLoader().getResource(name);
      return Files.lines(Paths.get(path.getPath()));
    }
    catch (Exception e) {
      throw new IllegalArgumentException("Failed to load file from resources with the name: \"" + name + "\"", e);
    }
  }
}
