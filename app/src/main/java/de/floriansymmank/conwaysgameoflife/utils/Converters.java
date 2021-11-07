package de.floriansymmank.conwaysgameoflife.utils;

import androidx.databinding.InverseMethod;

import java.math.BigDecimal;

public class Converters {
    public static int convertStringToInt(String text) {
        int i = 0;
        try {
            i = Integer.parseInt(text);
        } catch (Exception ignored) {
        }
        return i;
    }

    @InverseMethod(value = "convertStringToInt")
    public static String convertIntToString(int value) {
        return Integer.toString(value);
    }

    public static BigDecimal convertStringToBigDecimal(String text) {
       return new BigDecimal(text);
    }

    @InverseMethod(value = "convertStringToBigDecimal")

    public static String convertBigDeciamlToString(BigDecimal value) {

        if(value == null)
            value = new BigDecimal(0);

        return value.toPlainString();
    }
}
