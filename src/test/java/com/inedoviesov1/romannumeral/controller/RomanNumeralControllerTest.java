package com.inedoviesov1.romannumeral.controller;

import com.inedoviesov1.romannumeral.service.RomanNumeralService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RomanNumeralController.class)
public class RomanNumeralControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RomanNumeralService romanNumeralService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public MeterRegistry meterRegistry() {
            return new SimpleMeterRegistry();
        }

    }

    @Test
    public void testIntToRoman_shouldReturnValidResponse() throws Exception {
        when(romanNumeralService.intToRoman(1)).thenReturn("I");

        this.mockMvc.perform(get("/romannumeral").param("query", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.input").value(equalTo("1")))
                .andExpect(jsonPath("$.output").value(equalTo("I")));
    }

    @Test
    public void whenMissingParameter_thenReturns400() throws Exception {
        this.mockMvc.perform(get("/romannumeral"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Missing required parameter 'query'"));
    }

    @Test
    public void whenParameterIsOfWrongType_thenReturns400() throws Exception {
        this.mockMvc.perform(get("/romannumeral").param("query", "qwerty"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Parameter 'query' should be a number"));
    }

    @Test
    public void whenInputNumberIsOutOfRange_thenReturns400() throws Exception {
        when(romanNumeralService.intToRoman(0)).thenThrow(new IllegalArgumentException("Parameter 'query' should be in the range 1-3999"));
        when(romanNumeralService.intToRoman(4000)).thenThrow(new IllegalArgumentException("Parameter 'query' should be in the range 1-3999"));

        this.mockMvc.perform(get("/romannumeral").param("query", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Parameter 'query' should be in the range 1-3999"));

        this.mockMvc.perform(get("/romannumeral").param("query", "4000"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Parameter 'query' should be in the range 1-3999"));
    }
}
