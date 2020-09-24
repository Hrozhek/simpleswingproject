package service.account;

import converter.AccountDtoToAccountConverter;
import core.exceptions.DuplicateAccountNameException;
import core.exceptions.EntityNotFoundException;
import dto.account.AccountDto;
import model.Account;
import repo.account.AccountRepo;

import java.util.List;

public class AccountServiceImpl implements AccountService {
    private AccountRepo repo;

    public AccountServiceImpl(AccountRepo repo) {
        this.repo = repo;
    }

    @Override
    public Account getById(Long id) throws EntityNotFoundException {
        if (id == null)
            throw new RuntimeException("Id for search cannot be null");
        return repo.findById(id);
    }

    @Override
    public Account getByName(String name) throws EntityNotFoundException {
        return repo.findByName(name).orElseThrow(() -> new EntityNotFoundException(name));
    }

    @Override
    public List<Account> getAll() {
        return repo.findAll();
    }

    @Override
    public Long save(AccountDto accountDto) {
        if (repo.findByName(accountDto.getName()).isPresent())
            throw new DuplicateAccountNameException(accountDto.getName());

        Account account = AccountDtoToAccountConverter.convert(accountDto);
        repo.save(account);
        return account.getId();
    }
}
