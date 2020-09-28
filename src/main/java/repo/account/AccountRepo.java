package repo.account;

import dto.account.AccountDto;
import model.data.Account;

import java.util.List;

public interface AccountRepo {

    Account findById(long id);

    Account findByName(String name);

    long save(AccountDto accountDto);

    List<Account> findAll();
}
