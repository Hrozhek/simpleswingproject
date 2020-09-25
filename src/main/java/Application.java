import repo.account.AccountRepoCollectionImpl;
import repo.bid.BidRepoMapImpl;
import service.account.AccountService;
import service.account.AccountServiceImpl;
import service.bid.BidService;
import service.bid.BidServiceImpl;
import uidelegates.RootWindow;

import javax.swing.SwingUtilities;

public class Application {

    public static void main(String[] args) {
        AccountService accountService = new AccountServiceImpl(AccountRepoCollectionImpl.getInstance());
        BidService bidService = new BidServiceImpl(BidRepoMapImpl.getInstance(), accountService);
        SwingUtilities.invokeLater(() -> new RootWindow(accountService, bidService));
    }
}
