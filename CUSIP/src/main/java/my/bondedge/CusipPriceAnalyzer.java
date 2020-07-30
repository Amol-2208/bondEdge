package my.bondedge;

import com.google.common.util.concurrent.AtomicDouble;
import lombok.extern.slf4j.Slf4j;
import service.CusipPrice;
import service.CusipPriceImpl;

import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class CusipPriceAnalyzer {

    public static void main(String[] args) throws Exception {
        CusipPrice cusipPrice = new CusipPriceImpl();
        String fileName = "cusip.txt";
        log.info("Initiating call to retrieve Cusip to latest Price Mapping.");
        Path path = cusipPrice.checkFileConstraints(fileName);
        Optional<Map<String, AtomicDouble>> result = cusipPrice.findAllCusipsAndPrices(path);

        for (Map.Entry<String, AtomicDouble> entry : result.get().entrySet()) {
            System.out.println("CUSIP:  " + entry.getKey() + "---" + "Latest Price is: " + entry.getValue());
        }
    }
}
