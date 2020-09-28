package service.integration;

import converter.BidDtoToBidConverter;
import core.exceptions.EntityNotFoundException;
import dto.account.AccountDto;
import dto.bid.BidDto;
import model.data.Account;
import model.data.Bid;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import repo.account.AccountRepo;
import repo.account.AccountRepoCollectionImpl;
import repo.bid.BidRepo;
import repo.bid.BidRepoMapImpl;
import service.account.AccountService;
import service.account.AccountServiceImpl;
import service.bid.BidService;
import service.bid.BidServiceImpl;

import java.math.BigDecimal;

public class BidServiceIntegrationTest {
    private static final AccountRepo accountRepo;
    private static final AccountService accountService;
    private static final BidRepo bidRepo;
    private static final BidService bidService;
    private static final Account existingAccount;

    static {
        accountRepo = AccountRepoCollectionImpl.getInstance();
        accountService = new AccountServiceImpl(accountRepo);
        bidRepo = BidRepoMapImpl.getInstance();
        bidService = new BidServiceImpl(bidRepo, accountService);
        existingAccount = accountService.getById(accountService.save(new AccountDto("existInBidService")));
    }

    @Test
    public void save_provideCorrectDto_saveSuccessfully() {
        BidDto bidDto = new BidDto(existingAccount.getId(), "TEST", BigDecimal.valueOf(150.00), 3);
        Bid expectedBid = BidDtoToBidConverter.convert(bidDto);

        Bid actualBid = bidService.getById(bidService.save(bidDto));

        Assertions.assertEquals(expectedBid, actualBid);
    }

    @Test
    public void save_provideDtoWithAbsentAccount_throwEntityNotFoundException() {
        long nonExistingAccountId = -1L;
        BidDto incorrectBidDto = new BidDto(nonExistingAccountId, "NONE", BigDecimal.ZERO, 0);

        Assertions.assertThrows(EntityNotFoundException.class, () ->
            bidService.save(incorrectBidDto));

    }
}
