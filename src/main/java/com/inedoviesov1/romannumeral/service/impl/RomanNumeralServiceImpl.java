package com.inedoviesov1.romannumeral.service.impl;

import com.inedoviesov1.romannumeral.service.RomanNumeralService;
import org.springframework.stereotype.Service;

@Service
public class RomanNumeralServiceImpl implements RomanNumeralService {

    @Override
    public String intToRoman(int number) {
        if (number < 1 || number > 3999) {
            throw new IllegalArgumentException("Value should be in the range 1-3999");
        }

        int[] arabic = new int[]{1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] roman = new String[]{"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

        StringBuilder builder = new StringBuilder();
        int rest = number;

        for (int i = 0; i < arabic.length; i++) {
            int quotient = rest / arabic[i];
            builder.append(roman[i].repeat(quotient));
            rest = rest - arabic[i] * quotient;
        }

        return builder.toString();
    }
}
