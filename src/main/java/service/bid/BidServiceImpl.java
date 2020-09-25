package service.bid;

import converter.BidDtoToBidConverter;
import core.ApplicationConstants;
import core.exceptions.EntityNotFoundException;
import dto.bid.BidDto;
import model.data.Bid;
import repo.bid.BidRepo;
import service.account.AccountService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class BidServiceImpl implements BidService {
    private BidRepo repo;
    private AccountService accountService;

    public BidServiceImpl(BidRepo repo, AccountService accountService) {
        this.repo = repo;
        this.accountService = accountService;
    }

    @Override
    public Bid getById(Long id) {
        if (id == null)
            throw new IllegalArgumentException("Id for search cannot be null");
        return Optional.ofNullable(repo.findById(id))
            .orElseThrow(() -> new EntityNotFoundException(id));
    }

    @Override
    public List<Bid> getAllByAccountId(Long accountId) {
        if (accountId == null)
            throw new IllegalArgumentException("Id for search cannot be null");
        if (accountId == ApplicationConstants.EMPTY_ENTITY_ID)
            return Collections.emptyList();
        tryGetAccountById(accountId);
        return repo.findAllByAccountId(accountId);
    }

    @Override
    public Long save(BidDto bidDto) {
        tryGetAccountById(bidDto.getAccountId());

        Bid bid = BidDtoToBidConverter.convert(bidDto);
        repo.save(bid);
        return bid.getId();
    }

    private void tryGetAccountById(Long accountId) throws EntityNotFoundException {
            accountService.getById(accountId);
    }
}
