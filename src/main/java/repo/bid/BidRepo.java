package repo.bid;

import model.data.Bid;

import java.util.List;

public interface BidRepo {

    Bid findById(long id);

    List<Bid> findAllByAccountId(long id);

    void save(Bid bid);
}
