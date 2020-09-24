package service.bid;

import converter.BidDtoToBidConverter;
import core.Constants;
import core.exceptions.EntityNotFoundException;
import core.exceptions.ForeignKeyConstraintException;
import dto.bid.BidDto;
import model.Bid;
import repo.bid.BidRepo;
import service.account.AccountService;

import java.util.Collections;
import java.util.List;

public class BidServiceImpl implements BidService {
    private BidRepo repo;
    private AccountService accountService;

    public BidServiceImpl(BidRepo repo, AccountService accountService) {
        this.repo = repo;
        this.accountService = accountService;
    }

    @Override
    public Bid getById(Long id) throws EntityNotFoundException {
        if (id == null)
            throw new RuntimeException("Id for search cannot be null");
        return repo.findById(id);
    }

    @Override
    public List<Bid> getAllByAccountId(Long id) throws EntityNotFoundException {
        if (id == null)
            throw new RuntimeException("Id for search cannot be null");
        if (id == Constants.EMPTY_ENTITY_ID)
            return Collections.emptyList();
        accountService.getById(id);
        return repo.findAllByAccountId(id);
    }

    @Override
    public Long save(BidDto bidDto) throws ForeignKeyConstraintException {
        tryGetAccountById(bidDto.getAccountId());

        Bid bid = BidDtoToBidConverter.convert(bidDto);
        repo.save(bid);
        return bid.getId();
    }

    private void tryGetAccountById(Long accountId) {
        try {
            accountService.getById(accountId);
        } catch (EntityNotFoundException e) {
            RuntimeException fkException = new ForeignKeyConstraintException(accountId);
            fkException.initCause(e);
            throw fkException;
        }
    }
}
