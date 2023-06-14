package com.inedoviesov1.romannumeral.service;

/**
 * Service to convert Integers to and from Roman Numerals.
 */
public interface RomanNumeralService {

    /**
     * Converts given integer to a roman numeral.
     *
     * @param number integer number
     * @return A string representing a roman numeral
     * @throws IllegalArgumentException if number is smaller than 1 or bigger than 3999
     */
    String intToRoman(int number) throws IllegalArgumentException;
}
