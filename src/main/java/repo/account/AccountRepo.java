package repo.account;

import model.data.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepo {

    Account findById(long id);

    Account findByName(String name);

    void save(Account account);

    List<Account> findAll();
}
