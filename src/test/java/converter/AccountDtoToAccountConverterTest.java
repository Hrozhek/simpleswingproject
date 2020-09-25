package converter;

import dto.account.AccountDto;
import model.data.Account;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AccountDtoToAccountConverterTest {
    private static final String ACCOUNT_NAME = "test";
    private Account correctAccount;

    @BeforeEach
    private void setUp() {
        correctAccount = new Account(1000L, ACCOUNT_NAME);
    }

    @Test
    public void convert_provideCorrectDto_returnCorrectAccount() {
        AccountDto correctDto = new AccountDto(ACCOUNT_NAME);
        Account actualAccount = AccountDtoToAccountConverter.convert(correctDto);
        Assertions.assertEquals(correctAccount, actualAccount);
    }
}