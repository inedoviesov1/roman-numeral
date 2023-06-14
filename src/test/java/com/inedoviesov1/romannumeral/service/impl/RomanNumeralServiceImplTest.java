package com.inedoviesov1.romannumeral.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class RomanNumeralServiceImplTest {

    RomanNumeralServiceImpl service = new RomanNumeralServiceImpl();

    /**
     * Should throw an exception when a parameter is smaller than 1 or greater than 3999.
     */
    @Test
    void testIntToRoman_NumberOutOfRange_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> service.intToRoman(0));
        assertThrows(IllegalArgumentException.class, () -> service.intToRoman(4000));
    }

    /**
     * Should pass the tests with all the mappings from integer to roman numerals.
     */
    @ParameterizedTest
    @MethodSource("intToRomanMapping")
    void testIntToRoman_Parameterized(int integer, String roman) {
        assertEquals(roman, service.intToRoman(integer));
    }

    private static Stream<Arguments> intToRomanMapping() {
        return Stream.of(
                arguments(1, "I"),
                arguments(2, "II"),
                arguments(4, "IV"),
                arguments(5, "V"),
                arguments(7, "VII"),
                arguments(9, "IX"),
                arguments(10, "X"),
                arguments(11, "XI"),
                arguments(15, "XV"),
                arguments(50, "L"),
                arguments(100, "C"),
                arguments(500, "D"),
                arguments(1000, "M"),
                arguments(31, "XXXI"),
                arguments(148, "CXLVIII"),
                arguments(294, "CCXCIV"),
                arguments(312, "CCCXII"),
                arguments(421, "CDXXI"),
                arguments(528, "DXXVIII"),
                arguments(621, "DCXXI"),
                arguments(782, "DCCLXXXII"),
                arguments(870, "DCCCLXX"),
                arguments(941, "CMXLI"),
                arguments(1043, "MXLIII"),
                arguments(1110, "MCX"),
                arguments(1226, "MCCXXVI"),
                arguments(1301, "MCCCI"),
                arguments(1485, "MCDLXXXV"),
                arguments(1509, "MDIX"),
                arguments(1607, "MDCVII"),
                arguments(1754, "MDCCLIV"),
                arguments(1832, "MDCCCXXXII"),
                arguments(1993, "MCMXCIII"),
                arguments(2074, "MMLXXIV"),
                arguments(2152, "MMCLII"),
                arguments(2212, "MMCCXII"),
                arguments(2343, "MMCCCXLIII"),
                arguments(2499, "MMCDXCIX"),
                arguments(2574, "MMDLXXIV"),
                arguments(2646, "MMDCXLVI"),
                arguments(2723, "MMDCCXXIII"),
                arguments(2892, "MMDCCCXCII"),
                arguments(2975, "MMCMLXXV"),
                arguments(3051, "MMMLI"),
                arguments(3185, "MMMCLXXXV"),
                arguments(3250, "MMMCCL"),
                arguments(3313, "MMMCCCXIII"),
                arguments(3408, "MMMCDVIII"),
                arguments(3501, "MMMDI"),
                arguments(3610, "MMMDCX"),
                arguments(3743, "MMMDCCXLIII"),
                arguments(3844, "MMMDCCCXLIV"),
                arguments(3888, "MMMDCCCLXXXVIII"),
                arguments(3940, "MMMCMXL"),
                arguments(3999, "MMMCMXCIX")
        );
    }
}
