package repo.account;

import converter.AccountDtoToAccountConverter;
import dto.account.AccountDto;
import model.data.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class AccountRepoCollectionImpl implements AccountRepo {
    private static final ReadWriteLock LOCK = new ReentrantReadWriteLock();
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
        try {
            LOCK.readLock().lock();
            return storage.stream()
                .filter(account -> account.getId() == id)
                .findAny().orElse(null);
        } finally {
            LOCK.readLock().unlock();
        }
    }

    @Override
    public Account findByName(String name) {
        try {
            LOCK.readLock().lock();
            return storage.stream()
                .filter(account -> account.getName().equals(name))
                .findAny().orElse(null);
        } finally {
            LOCK.readLock().unlock();
        }
    }

    @Override
    public List<Account> findAll() {
        try {
            LOCK.readLock().lock();
            return new ArrayList<>(storage);
        } finally {
            LOCK.readLock().unlock();
        }
    }

    @Override
    public long save(AccountDto accountDto) {
        Account account = AccountDtoToAccountConverter.convert(accountDto);
        try {
            LOCK.writeLock().lock();
            storage.add(account);
        } finally {
            LOCK.writeLock().unlock();
        }
        return account.getId();
    }
}
