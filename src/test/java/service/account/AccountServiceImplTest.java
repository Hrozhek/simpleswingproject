package service.account;

import core.exceptions.DuplicateAccountNameException;
import core.exceptions.EntityNotFoundException;
import dto.account.AccountDto;
import model.data.Account;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import repo.account.AccountRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

class AccountServiceImplTest {
    @Mock private AccountRepo repo;
    private AccountService accountService;
    private Account testAccount;

    @BeforeEach
    private void init() {
        MockitoAnnotations.initMocks(this);
        accountService = new AccountServiceImpl(repo);
        testAccount = new Account(ThreadLocalRandom.current().nextLong(), "test");
    }

    @Test
    public void getById_provideCorrectId_returnCorrectAccount() {
        Mockito.when(repo.findById(testAccount.getId())).thenReturn(testAccount);

        Account actualAccount = accountService.getById(testAccount.getId());

        Assertions.assertEquals(testAccount, actualAccount);
        Mockito.verify(repo).findById(testAccount.getId());
    }

    @Test
    public void getById_provideAbsentId_throwEntityNotFoundException() {
        long absentId = ThreadLocalRandom.current().nextLong((testAccount.getId() + 1L), (testAccount.getId() + 100L));

        Mockito.when(repo.findById(absentId)).thenReturn(null);

        Assertions.assertThrows(EntityNotFoundException.class, () ->
            accountService.getById(absentId));
        Mockito.verify(repo).findById(absentId);
    }

    @Test
    public void getAll_containAccountList_returnSuccessfully() {
        List<Account> accounts = new ArrayList<>(5);
        for (int i = 0; i < 5; i++)
            accounts.add(testAccount);
        Mockito.when(repo.findAll()).thenReturn(accounts);

        List<Account> actualAccounts = accountService.getAll();

        Assertions.assertEquals(accounts, actualAccounts);
        Mockito.verify(repo).findAll();
    }

    @Test
    public void getByName_provideCorrectName_returnCorrectAccount() {
        Mockito.when(repo.findByName(testAccount.getName())).thenReturn(testAccount);

        Account actualAccount = accountService.getByName(testAccount.getName());

        Assertions.assertEquals(testAccount, actualAccount);
        Mockito.verify(repo).findByName(testAccount.getName());
    }

    @Test
    public void getByName_provideAbsentName_throwEntityNotFoundException() {
        String absentName = "Darth";

        Mockito.when(repo.findByName(absentName)).thenReturn(null);

        Assertions.assertThrows(EntityNotFoundException.class, () ->
            accountService.getByName(absentName));
        Mockito.verify(repo).findByName(absentName);
    }

    @Test
    public void save_provideCorrectDto_saveSuccessfully() {
        AccountDto dto = new AccountDto("Starkiller");
        long idAfterSave = ThreadLocalRandom.current().nextLong();
        Mockito.when(repo.save(dto)).thenReturn(idAfterSave);

        long actualId = accountService.save(dto);

        Assertions.assertEquals(idAfterSave, actualId);
        Mockito.verify(repo).save(dto);
    }

    @Test
    public void save_provideDtoWithDuplicateName_throwException() {
        AccountDto duplicateNameDto = new AccountDto(testAccount.getName());
        Mockito.when(repo.findByName(testAccount.getName())).thenReturn(testAccount);

        Assertions.assertThrows(DuplicateAccountNameException.class, () ->
            accountService.save(duplicateNameDto));
    }
}