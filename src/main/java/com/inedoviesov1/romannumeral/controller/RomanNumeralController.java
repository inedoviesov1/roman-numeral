package com.inedoviesov1.romannumeral.controller;

import com.inedoviesov1.romannumeral.model.TransformationResult;
import com.inedoviesov1.romannumeral.service.RomanNumeralService;
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

    @Autowired
    private RomanNumeralService romanNumeralService;

    @GetMapping("/romannumeral")
    public ResponseEntity<TransformationResult> intToRoman(@RequestParam("query") @NotNull(message = "Missing required parameter 'query'") Integer query) {
        String romanNumeral = romanNumeralService.intToRoman(query);
        TransformationResult result = new TransformationResult(String.valueOf(query), romanNumeral);

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
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
