package com.nahulemes.restaurantmanagement.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RoundToTwoDecimals {

    private RoundToTwoDecimals() {
    }

    public static float roundToTwoDecimals(float value) {
        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).floatValue();
    }
}
