package com.inedoviesov1.romannumeral.controller;

import com.inedoviesov1.romannumeral.service.RomanNumeralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RomanNumeralController {

    @Autowired
    private RomanNumeralService romanNumeralService;

    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @GetMapping("/romannumeral")
    public ResponseEntity<Object> intToRoman(@RequestParam("query") String query) {
        return new ResponseEntity<>(romanNumeralService.intToRoman(Integer.parseInt(query)), HttpStatus.OK);
    }

}
