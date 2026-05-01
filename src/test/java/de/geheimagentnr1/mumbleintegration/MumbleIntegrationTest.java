package de.geheimagentnr1.mumbleintegration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MumbleIntegrationTest {

    @Test
    void modIdIsValid() {

        String modId = "mumbleintegration";
        assertTrue( modId.matches( "[a-z][a-z0-9_]{1,63}" ) );
    }
}
