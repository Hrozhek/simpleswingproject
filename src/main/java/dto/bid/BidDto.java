package dto.bid;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class BidDto {
    private final Long accountId;
    private final String stockName;
    private final BigDecimal bidPrice;
    private final long stockQuantity;
}
