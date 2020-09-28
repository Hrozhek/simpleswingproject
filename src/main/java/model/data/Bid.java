package model.data;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import utils.PurchaseCostCalculator;

import java.math.BigDecimal;

@Getter
@Builder
@EqualsAndHashCode
public final class Bid {
    @EqualsAndHashCode.Exclude private final Long id;
    private final Long accountId;

    private final String stockName;
    private final BigDecimal bidPrice;
    private final long stockQuantity;

    public BigDecimal getPurchaseCost() {
        return PurchaseCostCalculator.calculate(bidPrice, stockQuantity);
    }
}
