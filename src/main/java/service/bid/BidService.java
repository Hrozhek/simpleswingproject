package service.bid;

import dto.bid.BidDto;
import model.data.Bid;

import java.util.List;

public interface BidService {
    Bid getById(Long id);

    List<Bid> getAllByAccountId(Long id);

    Long save(BidDto bidDto);
}
