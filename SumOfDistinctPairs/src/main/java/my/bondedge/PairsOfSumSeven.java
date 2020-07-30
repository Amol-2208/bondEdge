package my.bondedge;

import lombok.extern.slf4j.Slf4j;
import service.PairOfSumSevenService;
import service.PairOfSumSevenServiceImpl;

@Slf4j
public class PairsOfSumSeven {

    public static void main(String[] args) {

        log.info("Loading  all Positive Integers..");
        int[] input = { 1,2,3,4,5,6};
        PairOfSumSevenService pairOfSumSevenService = new PairOfSumSevenServiceImpl();

        log.info("Brute Force Approach");
        pairOfSumSevenService.findAllDistinctPairs(input,7);
        log.info("Using Integer  Streams");
        pairOfSumSevenService.getCountTwoSumUnique(input,7);
    }
}
