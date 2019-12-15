package se.iths.auktionera;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.iths.auktionera.api.controller.AccountController;
import se.iths.auktionera.api.controller.AuctionController;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class AuktioneraApplicationTests {

    @Autowired
    private AccountController accountController;

    @Autowired
    private AuctionController auctionController;

    @Test
    void contextLoads() {

        assertNotNull(accountController);
        assertNotNull(auctionController);
    }

}
