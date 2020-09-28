package converter;

import dto.bid.BidDto;
import model.data.Bid;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

class BidDtoToBidConverterTest {
    private final Long accountId = 1L;

    @Test
    public void  convert_provideCorrectDto_returnCorrectBid() {
        BidDto dto = new BidDto(accountId, "STCK", BigDecimal.TEN, 1);
        Bid expectedBid = getExpectedBid();

        Bid actualBid = BidDtoToBidConverter.convert(dto);

        Assertions.assertEquals(expectedBid, actualBid);
    }

    private Bid getExpectedBid() {
        long bidId = ThreadLocalRandom.current().nextLong();

        return Bid.builder()
            .id(bidId)
            .accountId(accountId)
            .stockName("STCK")
            .stockQuantity(1)
            .bidPrice(BigDecimal.TEN)
            .build();


    }
}