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
    private static volatile BidRepoMapImpl instance;
    private final Map<Long, Bid> bidMap;
    private final Map<Long, List<Bid>> bidsByAccountMap;
    private final ReadWriteLock lock;

    private BidRepoMapImpl() {
        bidMap = new HashMap<>();
        bidsByAccountMap = new HashMap<>();
        lock = new ReentrantReadWriteLock();
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
            lock.readLock().lock();
            return bidMap.get(id);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<Bid> findAllByAccountId(long id) {
        try {
            lock.readLock().lock();
            return bidsByAccountMap.getOrDefault(id, Collections.emptyList());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public long save(BidDto bidDto) {
        Bid bid = BidDtoToBidConverter.convert(bidDto);
        List<Bid> bidsByAccount;
        try {
            lock.writeLock().lock();
            bidMap.put(bid.getId(), bid);
            bidsByAccount = bidsByAccountMap.get(bid.getAccountId());
            if (bidsByAccount == null)
                bidsByAccount = new ArrayList<>();
            bidsByAccount.add(bid);
            bidsByAccountMap.put(bid.getAccountId(), bidsByAccount);
        } finally {
            lock.writeLock().unlock();
        }
        return bid.getId();
    }
}
