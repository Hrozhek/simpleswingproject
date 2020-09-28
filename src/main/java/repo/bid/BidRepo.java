package repo.bid;

import dto.bid.BidDto;
import model.data.Bid;

import java.util.List;

public interface BidRepo {

    Bid findById(long id);

    List<Bid> findAllByAccountId(long id);

    long save(BidDto bidDto);
}
