package com.inedoviesov1.romannumeral.controller;

import com.inedoviesov1.romannumeral.model.TransformationResult;
import com.inedoviesov1.romannumeral.service.RomanNumeralService;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.constraints.NotNull;

/**
 * Controller to handle requests to "/romannumeral"
 */
@RestController
public class RomanNumeralController {

    public static final String METRIC_ID_ROMANNUMERAL_REQUESTS = "romannumeral.requests.inttoroman";

    private static final Logger LOGGER = LogManager.getLogger(RomanNumeralController.class);

    @Autowired
    private RomanNumeralService romanNumeralService;

    @Autowired
    private MeterRegistry meterRegistry;

    @PostConstruct
    public void init() {
        LOGGER.info("RomanNumeralController initialized");

        LOGGER.info("Initializing a metric [{}]", METRIC_ID_ROMANNUMERAL_REQUESTS);
        meterRegistry.counter(METRIC_ID_ROMANNUMERAL_REQUESTS);
    }

    @GetMapping("/romannumeral")
    public ResponseEntity<TransformationResult> intToRoman(@RequestParam("query") @NotNull(message = "Missing required parameter 'query'") Integer query) {
        LOGGER.info("[GET] - /romannumeral - INPUT: [{}]", query);

        String romanNumeral = romanNumeralService.intToRoman(query);
        TransformationResult result = new TransformationResult(String.valueOf(query), romanNumeral);

        meterRegistry.counter(METRIC_ID_ROMANNUMERAL_REQUESTS, "outcome", "SUCCESS").increment();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    // Exception Handlers

    /**
     * Handle an exception when the request is missing a parameter.
     *
     * @param ex exception object
     * @return response entity
     */
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity<Object> handleMissingParameter(MissingServletRequestParameterException ex) {
        LOGGER.warn("[GET] - /romannumeral - missing required parameter [{}]", ex.getParameterName());
        meterRegistry.counter(METRIC_ID_ROMANNUMERAL_REQUESTS, "outcome", "ERROR", "cause", "missing-param").increment();

        return new ResponseEntity<>(String.format("Missing required parameter '%s'", ex.getParameterName()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle an exception when a parameter is of the wrong type.
     *
     * @param ex exception object
     * @return response entity
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleParameterTypeMismatch(MethodArgumentTypeMismatchException ex) {
        LOGGER.warn("[GET] - /romannumeral - parameter [{}] should be a number", ex.getName());
        meterRegistry.counter(METRIC_ID_ROMANNUMERAL_REQUESTS, "outcome", "ERROR", "cause", "type-mismatch").increment();

        return new ResponseEntity<>(String.format("Parameter '%s' should be a number", ex.getName()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle an exception when a parameter value is out of range.
     *
     * @param ex exception object
     * @return response entity
     */
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Object> handleConstraintViolation(IllegalArgumentException ex) {
        LOGGER.warn("[GET] - /romannumeral - {}", ex.getMessage());
        meterRegistry.counter(METRIC_ID_ROMANNUMERAL_REQUESTS, "outcome", "ERROR", "cause", "value-out-of-range").increment();

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
