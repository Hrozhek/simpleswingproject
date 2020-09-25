package repo.account;

import model.data.Account;

import java.util.ArrayList;
import java.util.List;

public final class AccountRepoCollectionImpl implements AccountRepo {
    private static volatile AccountRepoCollectionImpl instance;
    private final List<Account> storage;

    private AccountRepoCollectionImpl() {
        storage = new ArrayList<>();
    }

    public static AccountRepoCollectionImpl getInstance() {
        if (instance == null) {
            synchronized (AccountRepoCollectionImpl.class) {
                if (instance == null)
                    instance = new AccountRepoCollectionImpl();
            }
        }
        return instance;
    }

    @Override
    public Account findById(long id) {
        return storage.stream()
            .filter(account -> account.getId() == id)
            .findAny().orElse(null);
    }

    @Override
    public Account findByName(String name) {
        return storage.stream()
            .filter(account -> account.getName().equals(name))
            .findAny().orElse(null);
    }

    @Override
    public List<Account> findAll() {
        return new ArrayList<>(storage);
    }

    @Override
    public void save(Account account) {
        storage.add(account);
    }
}
