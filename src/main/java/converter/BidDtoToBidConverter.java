package converter;

import dto.bid.BidDto;
import model.data.Bid;
import utils.IdGenerator;

public class BidDtoToBidConverter {
    public static Bid convert(BidDto bidDto) {
        return Bid.builder()
            .id(IdGenerator.generateId())
            .accountId(bidDto.getAccountId())
            .stockName(bidDto.getStockName())
            .bidPrice(bidDto.getBidPrice())
            .stockQuantity(bidDto.getStockQuantity())
            .build();
    }
}
