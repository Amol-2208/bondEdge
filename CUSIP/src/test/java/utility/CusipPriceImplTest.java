package utility;

import com.google.common.util.concurrent.AtomicDouble;
import org.junit.Before;
import org.junit.Test;
import service.CusipPrice;
import service.CusipPriceImpl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

public class CusipPriceImplTest {
    private CusipPrice cusipPrice;

    @Before
    public void setUp(){
        cusipPrice = new CusipPriceImpl();
    }
    @Test(expected = FileSystemNotFoundException.class)
    public void testFindAllCusipsAndPrices_FileSizeZero() throws IOException, URISyntaxException {
        cusipPrice.checkFileConstraints("EmptyCusips.txt");
    }

    @Test
    public void testFindAllCusipsAndPrices_CusipWithNoPrice() throws IOException, URISyntaxException {
        Path path = cusipPrice.checkFileConstraints("cusip_NoPrice.txt");
        assertNotNull(path);

        Optional<Map<String, AtomicDouble>> allCusipsAndPrices = cusipPrice.findAllCusipsAndPrices(path);
        allCusipsAndPrices.get().forEach((k,v)->{
            assertNotNull(v);
        });
    }

    @Test
    public void testFindAllCusipsAndPrices_NoCusips() throws IOException, URISyntaxException {
        Path path = cusipPrice.checkFileConstraints("NoCusip.txt");
        assertNotNull(path);

        Optional<Map<String, AtomicDouble>> allCusipsAndPrices = cusipPrice.findAllCusipsAndPrices(path);
        assertTrue(allCusipsAndPrices.isEmpty());
    }
}