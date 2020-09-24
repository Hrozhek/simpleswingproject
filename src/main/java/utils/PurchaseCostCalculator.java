package utils;

import java.math.BigDecimal;

public final class PurchaseCostCalculator {
    private PurchaseCostCalculator() {}

    public static BigDecimal calculate(BigDecimal bidPrice, long stockQuantity) {
        if (bidPrice.compareTo(BigDecimal.valueOf(0L)) < 0 || stockQuantity < 0)
            throw new IllegalArgumentException("Bid price and stock quantity cannot be than zero");
        return bidPrice.multiply(BigDecimal.valueOf(stockQuantity));
    }
}
