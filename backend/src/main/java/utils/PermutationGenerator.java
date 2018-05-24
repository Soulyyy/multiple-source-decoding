package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class PermutationGenerator {

  public static List<List<Integer>> generateAllBinaryPermutations(int length) {
    if(length <= 0) {
      return Collections.emptyList();
    }
    Queue<List<Integer>> integers = new LinkedList<>();
    List<List<Integer>> permutations = new ArrayList<>();
    integers.add(new ArrayList<>());
    while (!integers.isEmpty()) {
      List<Integer> current = integers.poll();
      if (current.size() == length) {
        permutations.add(current);
      }
      else {
        integers.addAll(getBinaryExpandedLists(current));
      }
    }
    return permutations;
  }

  public static List<List<Integer>> generateExpandedBinaryValues(int lenght) {
    List<Integer> zeroes = new ArrayList<>(Collections.nCopies(lenght, 0));
    List<Integer> ones = new ArrayList<>(Collections.nCopies(lenght, 1));
    List<List<Integer>> expandedPermutations = new ArrayList<>();
    expandedPermutations.add(zeroes);
    expandedPermutations.add(ones);
    return expandedPermutations;
  }

  private static List<List<Integer>> getBinaryExpandedLists(List<Integer> list) {
    List<Integer> zeroAdded = new ArrayList<>(list);
    zeroAdded.add(0);
    list.add(1);
    List<List<Integer>> lists = new ArrayList<>();
    lists.add(zeroAdded);
    lists.add(list);
    return lists;
  }
}
