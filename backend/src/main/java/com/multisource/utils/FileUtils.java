package com.multisource.utils;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtils {

  private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

  public static Stream<String> getResourceAsStream(String name) {
    try {
      URL path = FileUtils.class.getClassLoader().getResource(name);
      log.info("Loading file from " + path.getPath());
      return Files.lines(Paths.get(path.getPath()));
    }
    catch (Exception e) {
      throw new IllegalArgumentException("Failed to load file from resources with the name: \"" + name + "\"", e);
    }
  }
}
