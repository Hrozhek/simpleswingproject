package repo.bid;

import model.data.Bid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class BidRepoMapImpl implements BidRepo {
    private static volatile BidRepoMapImpl instance;
    private final Map<Long, Bid> bidMap;
    private final Map<Long, List<Bid>> bidsByAccountMap;

    private BidRepoMapImpl() {
        bidMap = new HashMap<>();
        bidsByAccountMap = new HashMap<>();
    }

    public static BidRepoMapImpl getInstance() {
        if (instance == null) {
            synchronized (BidRepoMapImpl.class) {
                if (instance == null)
                    instance = new BidRepoMapImpl();
            }
        }
        return instance;
    }

    @Override
    public Bid findById(long id) {
        return bidMap.get(id);
    }

    @Override
    public List<Bid> findAllByAccountId(long id) {
        List<Bid> bids = bidsByAccountMap.get(id);
        if (bids == null)
            return Collections.EMPTY_LIST;
        return bids;
    }

    @Override
    public void save(Bid bid) {
        bidMap.put(bid.getId(), bid);
        List<Bid> bidsByAccount = bidsByAccountMap.get(bid.getAccountId());
        if (bidsByAccount == null)
            bidsByAccount = new ArrayList<>();
        bidsByAccount.add(bid);
        bidsByAccountMap.put(bid.getAccountId(), bidsByAccount);
    }
}
