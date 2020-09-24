package dto.bid;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class BidDto {
    private Long accountId;
    private String stockName;
    private BigDecimal bidPrice;
    private long stockQuantity;
}
