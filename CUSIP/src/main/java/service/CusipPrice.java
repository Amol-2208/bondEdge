package service;

import com.google.common.util.concurrent.AtomicDouble;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

public interface CusipPrice {
    Path checkFileConstraints(String fileName) throws URISyntaxException, IOException;
    Optional<Map<String, AtomicDouble>>  findAllCusipsAndPrices(Path filePath) throws IOException, URISyntaxException;
}
