package repo.account;

import core.exceptions.EntityNotFoundException;
import model.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class AccountRepoCollectionImpl implements AccountRepo {
    private static AccountRepoCollectionImpl instance;
    private List<Account> storage;

    private AccountRepoCollectionImpl() {
        storage = new ArrayList<>();
    }

    public static AccountRepoCollectionImpl getInstance() {
        synchronized (AccountRepoCollectionImpl.class) {
            if (instance == null) {
                instance = new AccountRepoCollectionImpl();
            }
        }
        return instance;
    }

    @Override
    public Account findById(long id) {
        return storage.stream()
            .filter(account -> account.getId() == id)
            .findAny().orElseThrow(() -> new EntityNotFoundException(id));
    }

    @Override
    public Optional<Account> findByName(String name) {
        return storage.stream()
            .filter(account -> account.getName().equals(name))
            .findAny();
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
