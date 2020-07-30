package service;

import com.google.common.util.concurrent.AtomicDouble;
import lombok.extern.slf4j.Slf4j;
import my.bondedge.CusipPriceAnalyzer;
import my.bondedge.CusipPriceMappingModel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;

@Slf4j
public class CusipPriceImpl implements CusipPrice {
    private static final Pattern CUSIP_PATTERN = Pattern.compile("[A-Za-z0-9]{8}");
    private static final Pattern PRICE_PATTERN = Pattern.compile("[0-9]*(\\.[0-9]+)?");

    @Override
    public Optional<Map<String, AtomicDouble>> findAllCusipsAndPrices(Path filePath) throws IOException, URISyntaxException {
        Map<String, AtomicDouble> result = new Hashtable<>();
        Deque<String> deque = new LinkedList<>();

        log.info("All basic validation criterias for  input file are satisfied!");
        Files.lines(filePath).parallel().forEachOrdered(
                line -> {
                    log.info("Let's start to parse file line by line and find CUSIP and latest price available.");
                    if (CUSIP_PATTERN.matcher(line).matches()) {
                        if (deque.size() != 0) {

                            CusipPriceMappingModel cusipPriceMappingModel = mapCusipToPrice(deque);
                            result.put(cusipPriceMappingModel.getName(), cusipPriceMappingModel.getClosingPrice());
                        }

                        deque.clear();
                        deque.addFirst(line);
                    }

                    if (PRICE_PATTERN.matcher(line).matches()) {
                        deque.addFirst(line);
                    }

                }
        );
        log.info("All lines are processed. Now let's map the last remaining entry");
        CusipPriceMappingModel cusipPriceMappingModel = mapCusipToPrice(deque);
        if(!CUSIP_PATTERN.matcher(cusipPriceMappingModel.getName()).matches()){
            return Optional.empty();
        }
        result.put(cusipPriceMappingModel.getName(), cusipPriceMappingModel.getClosingPrice());
        log.info("File Parsing Success! Printing the  results now...");
        return Optional.of(result);
    }

    private static CusipPriceMappingModel mapCusipToPrice(Deque<String> cusipRec) {
        log.info("Mapping the  CUSIP to PRICE");
        if (cusipRec.size() > 1) {
            return new CusipPriceMappingModel(cusipRec.pollLast(), new AtomicDouble(Double.valueOf(cusipRec.getFirst())));
        }
        log.info("CUSIP does not have  any PRICE listed. Assigning the PRICE to 0.00");
        return new CusipPriceMappingModel(cusipRec.pollLast(), new AtomicDouble(Double.valueOf("0.0")));
    }

    @Override
    public Path checkFileConstraints(String fileName) throws URISyntaxException, IOException {

        log.info("Retrieving file contents...");
        URI fileUri = new URI("file:///" + Objects.requireNonNull(CusipPriceAnalyzer.class.getClassLoader().getResource(fileName)).getPath());
        Path filePath = Paths.get(fileUri);

        if (!Files.exists(Path.of(fileUri), LinkOption.NOFOLLOW_LINKS)) {
            log.error("File cannot be empty or null.");
            throw new FileNotFoundException();
        }

        if (Files.size(filePath) == 0) {
            log.error("Blank File Found!");
            throw new FileSystemNotFoundException("File Contents Empty.");
        }

        return filePath;
    }
}