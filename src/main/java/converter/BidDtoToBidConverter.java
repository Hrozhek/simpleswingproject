package converter;

import dto.bid.BidDto;
import model.Bid;
import utils.IdGenerator;
import utils.PurchaseCostCalculator;

import java.math.BigDecimal;

public class BidDtoToBidConverter {
    public static Bid convert(BidDto bidDto) {
        BigDecimal purchaseCost = PurchaseCostCalculator.calculate(bidDto.getBidPrice(), bidDto.getStockQuantity());

        return Bid.builder()
            .id(IdGenerator.generateId())
            .accountId(bidDto.getAccountId())
            .stockName(bidDto.getStockName())
            .bidPrice(bidDto.getBidPrice())
            .stockQuantity(bidDto.getStockQuantity())
            .purchaseCost(purchaseCost)
            .build();
    }
}
