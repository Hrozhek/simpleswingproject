package utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class PurchaseCostCalculatorTest {

    @Test
    void calculate_provideTwoPositiveNumbers_returnCorrectResult() {
        BigDecimal positiveBigDecimal = BigDecimal.TEN;
        long positiveLong = 10L;
        BigDecimal expectedBigDecimal = BigDecimal.valueOf(100L);
        BigDecimal actualBigDecimal = PurchaseCostCalculator.calculate(positiveBigDecimal, positiveLong);
        Assertions.assertEquals(expectedBigDecimal, actualBigDecimal);

    }

    @Test
    void calculate_provideNegativeNumbers_throwIllegalArgumentException() {
        BigDecimal negativeBigDecimal = BigDecimal.valueOf(-1L);
        long negativeLong = -1L;
        Assertions.assertThrows(IllegalArgumentException.class, () ->
            PurchaseCostCalculator.calculate(negativeBigDecimal, negativeLong));
    }
}