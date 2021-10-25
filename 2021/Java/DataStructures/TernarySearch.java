package Searches;

import static java.lang.String.format;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;
public class TernarySearch implements SearchAlgorithm {

  @Override
  public <T extends Comparable<T>> int find(T[] arr, T value) {
    return ternarySearch(arr, value, 0, arr.length - 1);
  }
  
  private <T extends Comparable<T>> int ternarySearch(T[] arr, T key, int start, int end) {
    if (start > end) {
      return -1;
    }
    int mid1 = start + (end - start) / 3;
    int mid2 = start + 2 * (end - start) / 3;

    if (key.compareTo(arr[mid1]) == 0) {
      return mid1;
    } else if (key.compareTo(arr[mid2]) == 0) {
      return mid2;
    }

    else if (key.compareTo(arr[mid1]) < 0) {
      return ternarySearch(arr, key, start, --mid1);
    }

    else if (key.compareTo(arr[mid2]) > 0) {
      return ternarySearch(arr, key, ++mid2, end);
    }

    else {
      return ternarySearch(arr, key, mid1, mid2);
    }
  }
  public static void main(String[] args) {
    Random r = new Random();
    int size = 100;
    int maxElement = 100000;
    Integer[] integers =
        Stream.generate(() -> r.nextInt(maxElement)).limit(size).sorted().toArray(Integer[]::new);

    Integer shouldBeFound = integers[r.nextInt(size - 1)];

    TernarySearch search = new TernarySearch();
    int atIndex = search.find(integers, shouldBeFound);

    System.out.println(
        format(
            "Should be found: %d. Found %d at index %d. An array length %d",
            shouldBeFound, integers[atIndex], atIndex, size));

    int toCheck = Arrays.binarySearch(integers, shouldBeFound);
    System.out.println(
        format(
            "Found by system method at an index: %d. Is equal: %b", toCheck, toCheck == atIndex));
  }
}
