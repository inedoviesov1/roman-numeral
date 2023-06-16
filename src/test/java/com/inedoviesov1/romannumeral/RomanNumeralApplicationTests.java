package com.inedoviesov1.romannumeral;

import com.inedoviesov1.romannumeral.controller.RomanNumeralController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class RomanNumeralApplicationTests {

    @Autowired
    private RomanNumeralController romanNumeralController;

    // Sanity check to ensure that the application context can start and required controllers are registered.
    @Test
    void contextLoads() {
        assertThat(romanNumeralController).isNotNull();
    }

}
