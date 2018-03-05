package functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import data.trellis.Trellis;
import data.trellis.TrellisNode;
import utils.PermutationGenerator;

public class ViterbiDecoder {

  private final Trellis trellis;

  public ViterbiDecoder(Trellis trellis) {
    this.trellis = trellis;
  }

  public List<Integer> decode(List<Integer> encoded, double errorRate) {
    int encodedLength = trellis.getNodes().entrySet().stream()
        .map(Map.Entry::getValue)
        .map(TrellisNode::getValue)
        .mapToInt(List::size)
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("Cannot infer encoded block length"));

    int elementLength = trellis.getNodes().entrySet().stream()
        .map(Map.Entry::getValue)
        .map(TrellisNode::getKey)
        .mapToInt(List::size)
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("Cannot infer encoded block length"));

    List<List<Integer>> states = PermutationGenerator.generateAllBinaryPermutations(elementLength);

    int decodeLength = encoded.size() / encodedLength;
    assert encoded.size() % encodedLength == 0;
    Map<List<Integer>, Map<List<Integer>, Double>> transitionProbabilities = getTransitionMap(elementLength);
    Map<List<Integer>, Map<List<Integer>, Double>> encodingProbabilities = getEncodingProbabilities(elementLength, encodedLength, errorRate);

    double[][] mostLikelyPath = new double[transitionProbabilities.size()][encoded.size() / encodedLength];
    Integer[][] mostLikelyResult = new Integer[transitionProbabilities.size()][encoded.size() / encodedLength];
    Arrays.stream(mostLikelyPath[0]).forEach(i -> i = 1.0);
    IntStream.range(0, mostLikelyPath.length).forEach(i -> mostLikelyPath[i][0] = 1.0);
    Arrays.stream(mostLikelyResult[0]).forEach(i -> i = 0);

    for (int i = 1; i < encoded.size() / encodedLength; i++) {
      List<Integer> encodedElement = encoded.subList(i * encodedLength, i * encodedLength + encodedLength);

      for (int j = 0; j < Math.ceil(Math.pow(2, elementLength)); j++) {
        //Compute max
        double curMax = -1;
        int argMax = -1;
        List<Map.Entry<List<Integer>, Map<List<Integer>, Double>>> transitionEntryList = new ArrayList<>(transitionProbabilities.entrySet());
        for (int k = 0; k < transitionProbabilities.size(); k++) {
          double transitionProbability = transitionEntryList.get(k).getValue().get(states.get(j));
          double encodingProbability = encodingProbabilities.get(states.get(j)).get(encodedElement);
          double value = mostLikelyPath[k][i - 1] * transitionProbability * encodingProbability;
          if (curMax < value) {
            curMax = value;
            argMax = k;
          }
        }
        mostLikelyPath[j][i] = curMax;
        mostLikelyResult[j][i] = argMax;
      }
    }
    //Reconstruct
    int lastElementIndex = -1;
    double lastElementProbability = -1;
    for (int i = 0; i < mostLikelyPath.length; i++) {
      double probability = mostLikelyPath[i][mostLikelyPath.length - 1];
      if (lastElementProbability < probability) {
        lastElementProbability = probability;
        lastElementIndex = mostLikelyResult[i][mostLikelyPath.length - 1];
      }
    }
    Integer[] resultPath = new Integer[encoded.size() / encodedLength];
    resultPath[resultPath.length - 1] = lastElementIndex;
    for (int i = 2; i <= resultPath.length; i++) {
      resultPath[resultPath.length - i] = mostLikelyResult[resultPath[resultPath.length - i + 1]][resultPath.length - i + 1];
    }
    List<Integer> decoded = new ArrayList<>();
    for (int i = 0; i < resultPath.length; i++) {
      decoded.addAll(states.get(resultPath[i]));
    }
    return decoded;
  }

  private Map<List<Integer>, Map<List<Integer>, Double>> getTransitionMap(int length) {
    List<List<Integer>> allElements = PermutationGenerator.generateAllBinaryPermutations(length);
    Map<List<Integer>, Map<List<Integer>, Double>> transitionMap = new HashMap<>();
    for (int i = 0; i < allElements.size(); i++) {
      List<Integer> element = allElements.get(i);
      List<Integer> elementSuffix = element.subList(1, element.size());
      Map<List<Integer>, Double> elementTransitionMap = new HashMap<>();
      for (int j = 0; j < allElements.size(); j++) {
        List<Integer> transitionElement = allElements.get(j);
        List<Integer> transitionElementPrefix = transitionElement.subList(0, transitionElement.size() - 1);
        if (transitionElementPrefix.equals(elementSuffix)) {
          elementTransitionMap.put(transitionElement, (1.0 * (transitionElementPrefix.size() + 1)) / allElements.size());
        }
        else {
          elementTransitionMap.put(transitionElement, 0.0);
        }
      }
      transitionMap.put(element, elementTransitionMap);
    }
    return transitionMap;
  }

  private Map<List<Integer>, Map<List<Integer>, Double>> getEncodingProbabilities(int length, int encodingLength, double errorRate) {
    Map<List<Integer>, Map<List<Integer>, Double>> encodingProbabilities = new HashMap<>();

    List<List<Integer>> allElements = PermutationGenerator.generateAllBinaryPermutations(length);
    for (int i = 0; i < allElements.size(); i++) {
      List<List<Integer>> allEncodingPermutations = PermutationGenerator.generateAllBinaryPermutations(encodingLength);
      Map<List<Integer>, Double> elementEncodingProbabilities = new HashMap<>();
      allEncodingPermutations.forEach(l -> elementEncodingProbabilities.put(l, 0.0));
      for (int j = 0; j < allElements.size(); j++) {
        long hammingDistance = computeHammingDistance(allElements.get(i), allElements.get(j));
        List<Integer> encodedValue = trellis.getNode(allElements.get(j)).getValue();
        double encodingProbability = computeEncodingProbability(allElements.get(j).size(), hammingDistance, errorRate);
        elementEncodingProbabilities.put(encodedValue, encodingProbability);
      }
      encodingProbabilities.put(allElements.get(i), elementEncodingProbabilities);
    }
    return encodingProbabilities;
  }

  private long computeHammingDistance(List<Integer> first, List<Integer> second) {
    if (first == null || second == null || first.size() != second.size()) {
      throw new IllegalArgumentException("Can't compute hamming distance of " + first + " and " + second);
    }
    return IntStream.range(0, first.size()).filter(i -> first.get(i).equals(second.get(i))).count();
  }

  private double computeEncodingProbability(long length, long hammingDistance, double errorRate) {
    if (length == hammingDistance) {
      return Math.pow(errorRate, hammingDistance);
    }
    else if (hammingDistance == 0) {
      return Math.pow(1 - errorRate, length);
    }
    return 1 - Math.pow(1 - errorRate, length - hammingDistance);
  }

}
