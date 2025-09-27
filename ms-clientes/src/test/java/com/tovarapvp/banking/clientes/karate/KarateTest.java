package com.tovarapvp.banking.clientes.karate;

import com.intuit.karate.junit5.Karate;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class KarateTest {

    @LocalServerPort
    private int port;

    @Test
    void testAll() {
        Karate.run().relativeTo(getClass()).systemProperty("server.port", "" + port).parallel(1);
    }
}