package service.integration;

import converter.AccountDtoToAccountConverter;
import dto.account.AccountDto;
import model.data.Account;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import repo.account.AccountRepo;
import repo.account.AccountRepoCollectionImpl;
import service.account.AccountService;
import service.account.AccountServiceImpl;

public class AccountServiceIntegrationTest {
    private static final AccountRepo accountRepo;
    private static final AccountService accountService;
    private static final Account existingAccount;


    static {
        accountRepo = AccountRepoCollectionImpl.getInstance();
        accountService = new AccountServiceImpl(accountRepo);
        existingAccount = accountService.getById(accountService.save(new AccountDto("existInAccountService")));
    }

    @Test
    public void save_provideCorrectDto_saveSuccessfully() {
        AccountDto accountDto = new AccountDto("test");
        Account expectedAccount = AccountDtoToAccountConverter.convert(accountDto);

        long testAccountId = accountService.save(accountDto);
        Account actualAccount = accountService.getById(testAccountId);

        Assertions.assertEquals(expectedAccount, actualAccount);
    }


    @Test
    public void getById_provideCorrectId_returnAccount() {
        Account actualAccount = accountService.getById(existingAccount.getId());

        Assertions.assertEquals(existingAccount, actualAccount);
    }

}
