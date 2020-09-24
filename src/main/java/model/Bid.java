package model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Builder
@ToString
public final class Bid {
    private final Long id;
    private final Long accountId;

    private final String stockName;
    private final BigDecimal bidPrice;
    private final long stockQuantity;
    private BigDecimal purchaseCost;
}
