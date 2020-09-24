package repo.bid;

import core.exceptions.EntityNotFoundException;
import model.Bid;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class BidRepoCollectionImpl implements BidRepo {
    private static BidRepoCollectionImpl instance;
    private List<Bid> storage;

    private BidRepoCollectionImpl() {
        storage = new ArrayList<>();
    }

    public static BidRepoCollectionImpl getInstance() {
        synchronized (BidRepoCollectionImpl.class) {
            if (instance == null) {
                instance = new BidRepoCollectionImpl();
            }
        }
        return instance;
    }

    @Override
    public Bid findById(long id) {
        return storage.stream()
            .filter(bid -> bid.getId() == id)
            .findAny().orElseThrow(() -> new EntityNotFoundException(id));
    }

    @Override
    public List<Bid> findAllByAccountId(long id) {
        return storage.stream()
            .filter(bid -> bid.getAccountId() == id)
            .collect(Collectors.toList());
    }

    @Override
    public void save(Bid bid) {
        storage.add(bid);
    }
}
