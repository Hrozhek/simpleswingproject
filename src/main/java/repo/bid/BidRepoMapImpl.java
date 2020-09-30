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
import java.util.stream.Collectors;

public final class BidRepoMapImpl implements BidRepo {
    private static volatile BidRepoMapImpl instance;
    private final Map<Long, Bid> bidMap;
    private final Map<Long, List<Long>> bidsIdByAccountMap;
    private final ReadWriteLock lock;

    private BidRepoMapImpl() {
        bidMap = new HashMap<>();
        bidsIdByAccountMap = new HashMap<>();
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
            List<Long> bidsId = bidsIdByAccountMap.getOrDefault(id, Collections.emptyList());
            return bidsId.stream()
                .map(bidMap::get)
                .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public long save(BidDto bidDto) {
        Bid bid = BidDtoToBidConverter.convert(bidDto);
        try {
            lock.writeLock().lock();
            bidMap.put(bid.getId(), bid);
            bidsIdByAccountMap.putIfAbsent(bid.getAccountId(), new ArrayList<>());
            bidsIdByAccountMap.get(bid.getAccountId()).add(bid.getId());
        } finally {
            lock.writeLock().unlock();
        }
        return bid.getId();
    }
}
