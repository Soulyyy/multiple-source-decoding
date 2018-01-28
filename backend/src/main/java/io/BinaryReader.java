package io;

public class BinaryReader {


  public String readString(String input) {
    return validateInput(input);
  }

  private String validateInput(String input) {
    if(input.length() == 0) {
      throw new IllegalArgumentException("Input is empty");
    }
    StringBuilder sb = new StringBuilder();
    input.chars().filter(i -> i == '0' || i == '1').forEach(i -> sb.append((char)i));
    if(sb.toString().length() != input.length()) {
      throw new IllegalArgumentException("Input contains non binary characters: \""+ input+"\"");
    }
      return input;
    }

}
