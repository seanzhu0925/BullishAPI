import org.api.bullish.service.CheckoutServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { ApplicationConfig.class })
@WebAppConfiguration
public class ElectronicStoreApplicationTest {


    @Autowired
    private CheckoutServiceImpl checkoutService;

    @BeforeAll
    static void initAll() {
        System.out.println("---Inside initAll---");
    }

    @BeforeEach
    void init(TestInfo testInfo) {
        System.out.println("Start..." + testInfo.getDisplayName());
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        System.out.println("Finished..." + testInfo.getDisplayName());
    }

    @Test
    public void messageTest() {
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("---Inside tearDownAll---");
    }

}
