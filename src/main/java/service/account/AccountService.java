package service.account;

import dto.account.AccountDto;
import model.data.Account;

import java.util.List;

public interface AccountService {
    Account getById(Long id);

    Account getByName(String name);

    List<Account> getAll();

    Long save(AccountDto accountDto);
}
