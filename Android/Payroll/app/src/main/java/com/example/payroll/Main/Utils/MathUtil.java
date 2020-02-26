package com.example.payroll.Main.Utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class MathUtil {

    public static double round (double num, int places) {
        if(places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(num));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
