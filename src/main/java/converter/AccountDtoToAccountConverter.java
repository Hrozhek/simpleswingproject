package converter;

import dto.account.AccountDto;
import model.Account;
import utils.IdGenerator;

public class AccountDtoToAccountConverter {
    public static Account convert(AccountDto accountDto) {
        return new Account(IdGenerator.generateId(), accountDto.getName());
    }
}
