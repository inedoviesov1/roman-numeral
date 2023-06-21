package com.inedoviesov1.romannumeral.service.impl;

import com.inedoviesov1.romannumeral.service.RomanNumeralService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class RomanNumeralServiceImpl implements RomanNumeralService {

    private static final Logger LOGGER = LogManager.getLogger(RomanNumeralServiceImpl.class);

    @Override
    public String intToRoman(int number) {
        LOGGER.debug("Start converting [{}] to roman numeral", number);
        if (number < 1 || number > 3999) {
            LOGGER.error("Number [{}] is out of allowed range, value won't be converted.", number);
            throw new IllegalArgumentException("Value should be in the range 1-3999, but provided value is " + number);
        }

        int[] arabic = new int[]{1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] roman = new String[]{"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

        StringBuilder builder = new StringBuilder();
        int rest = number;

        for (int i = 0; i < arabic.length; i++) {
            int quotient = rest / arabic[i];
            if (quotient > 0) {
                String repeat = roman[i].repeat(quotient);
                builder.append(repeat);
                rest = rest - arabic[i] * quotient;

                LOGGER.debug("{} -> {}", quotient * arabic[i], repeat);
            }
        }

        String resultStr = builder.toString();

        LOGGER.debug("Finished converting [{}] to roman numeral, result: [{}]", number, resultStr);

        return resultStr;
    }
}
