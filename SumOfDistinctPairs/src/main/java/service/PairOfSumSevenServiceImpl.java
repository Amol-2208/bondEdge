package service;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.IntStream;

@Slf4j
public class PairOfSumSevenServiceImpl implements PairOfSumSevenService {

    @Override
    public List<Integer> getCountTwoSumUnique(int[] nums, int target) {
        Set<Integer> unique = new HashSet<>();
        List<Integer> result = new ArrayList<>();

        for (int num : nums) {
            log.info("if both num and compliment is in set, (skip this)");
            unique.add(num);
        }

        for (int num : nums) {
            int comp = target - num;
            if (unique.contains(comp)) {
                result.add(num);
                log.info("If comp is in it, then num is as well since we put all of nums in the set.");
                unique.remove(num);
                unique.remove(comp);
            }
        }

        if (result.isEmpty()) {
            log.info("No Pairs found");
            return new ArrayList<>();
        }

        final StringBuilder output4 = new StringBuilder();
        result.parallelStream().forEach((pair) -> output4.append("{" + pair + ", " + (target - pair) + "}, "));
        System.out.println("Distinct Pairs: " + output4.toString().substring(0, output4.length() - 2));

        return result;
    }

    @Override
    public List<Integer> findAllDistinctPairs(int[] input, int sum) {
        final List<Integer> allDifferentPairs = new ArrayList<>();
        final Map<Integer, Integer> pairs = new HashMap<>();
        IntStream.range(0, input.length).forEach(i -> {
                    if (pairs.containsKey(input[i])) {
                        if (pairs.get(input[i]) != null) {
                            log.info("Add Pair to the list");
                            allDifferentPairs.add(input[i]);
                        }
                        log.info("Marking pair to prevent duplicates");
                        pairs.put(sum - input[i], null);
                    } else if (!pairs.containsValue(input[i])) {
                        pairs.put(sum - input[i], input[i]);
                    }
                }
        );

        if (allDifferentPairs.isEmpty()) {
            log.info("No Pairs found");
            return new ArrayList<>();
        }
        final StringBuilder output4 = new StringBuilder();
        allDifferentPairs.forEach((pair) -> output4.append("{" + pair + ", " + (sum - pair) + "}, "));
        System.out.println("Distinct  Pairs are: " + output4.toString().substring(0, output4.length() - 2));

        return allDifferentPairs;
    }
}
