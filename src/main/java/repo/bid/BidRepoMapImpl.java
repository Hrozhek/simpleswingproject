package repo.bid;

import converter.BidDtoToBidConverter;
import dto.bid.BidDto;
import model.data.Bid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class BidRepoMapImpl implements BidRepo {
    private static final ReadWriteLock LOCK = new ReentrantReadWriteLock();
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
        try {
            LOCK.readLock().lock();
            return bidMap.get(id);
        } finally {
            LOCK.readLock().unlock();
        }
    }

    @Override
    public List<Bid> findAllByAccountId(long id) {
        List<Bid> bids;
        try {
            LOCK.readLock().lock();
            bids = bidsByAccountMap.get(id);
        } finally {
            LOCK.readLock().unlock();
        }
        if (bids == null)
            return Collections.emptyList();
        return bids;
    }

    @Override
    public long save(BidDto bidDto) {
        Bid bid = BidDtoToBidConverter.convert(bidDto);
        List<Bid> bidsByAccount;
        try {
            LOCK.readLock().lock();
            bidMap.put(bid.getId(), bid);
            bidsByAccount = bidsByAccountMap.get(bid.getAccountId());
        } finally {
            LOCK.readLock().unlock();
        }
        if (bidsByAccount == null)
            bidsByAccount = new ArrayList<>();
        bidsByAccount.add(bid);
        try {
            LOCK.writeLock().lock();
            bidsByAccountMap.put(bid.getAccountId(), bidsByAccount);
        } finally {
            LOCK.writeLock().unlock();
        }
        return bid.getId();
    }
}
