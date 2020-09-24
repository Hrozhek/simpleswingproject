package integration;

import dto.account.AccountDto;
import dto.bid.BidDto;
import org.junit.jupiter.api.Test;
import repo.account.AccountRepoCollectionImpl;
import repo.bid.BidRepoCollectionImpl;
import service.account.AccountService;
import service.account.AccountServiceImpl;
import service.bid.BidService;
import service.bid.BidServiceImpl;

import java.math.BigDecimal;

//TODO
public class IntegrationTest {
    @Test
    public static void test() {
        AccountService accountService = new AccountServiceImpl(AccountRepoCollectionImpl.getInstance());
        BidService bidService = new BidServiceImpl(BidRepoCollectionImpl.getInstance(), accountService);
        AccountDto testAcc = new AccountDto("test");
        long testAccountId = accountService.save(testAcc);
        BidDto testBid = new BidDto(testAccountId, "TEST", BigDecimal.valueOf(150.00), 3);
        try {
            BidDto failedTestBid = new BidDto(null, "TEST", BigDecimal.valueOf(150.00), 3);
            long failedTestBidId = bidService.save(failedTestBid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long testBidId = bidService.save(testBid);
        System.out.println(bidService.getAllByAccountId(testAccountId + 222L));
    }
}
