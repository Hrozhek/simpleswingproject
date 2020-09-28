package service.bid;

import core.ApplicationConstants;
import core.exceptions.EntityNotFoundException;
import dto.bid.BidDto;
import model.data.Bid;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import repo.bid.BidRepo;
import service.account.AccountService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class BidServiceImplTest {
    @Mock BidRepo repo;
    @Mock AccountService accountService;
    BidService bidService;
    Bid testBid;

    @BeforeEach
    private void init() {
        MockitoAnnotations.initMocks(this);
        bidService = new BidServiceImpl(repo, accountService);
        testBid = getTestBid();
    }

    @Test
    public void getById_provideCorrectId_returnCorrectBid() {
        Mockito.when(repo.findById(testBid.getId())).thenReturn(testBid);

        Bid actualBid = bidService.getById(testBid.getId());

        Mockito.verify(repo).findById(testBid.getId());
        Assertions.assertEquals(testBid, actualBid);
    }

    @Test
    public void getById_provideIncorrectId_throwEntityNotFoundException() {
        long absentId = 1L;
        Mockito.when(repo.findById(absentId)).thenReturn(null);

        Assertions.assertThrows(EntityNotFoundException.class, () ->
            bidService.getById(absentId));
        Mockito.verify(repo).findById(absentId);
    }

    @Test
    public void getAllByAccountId_provideCorrectId_returnListWithBids() {
        long accountId = 100L;
        List<Bid> bids = new ArrayList<>(5);
        for (int i = 0; i < 5; i++)
            bids.add(testBid);
        Mockito.when(repo.findAllByAccountId(accountId)).thenReturn(bids);

        List<Bid> actualBids = bidService.getAllByAccountId(accountId);

        Mockito.verify(repo).findAllByAccountId(accountId);
        Assertions.assertEquals(bids, actualBids);
    }

    @Test
    public void getAllByAccountId_provideNullId_throwIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
            bidService.getById(null));
        Mockito.verifyZeroInteractions(repo);
    }

    @Test
    public void getAllByAccountId_provideEmptyEntityId_returnEmptyList() {
        List<Bid> emptyList = Collections.emptyList();

        List<Bid> actualList = bidService.getAllByAccountId(ApplicationConstants.EMPTY_ENTITY_ID);

        Assertions.assertEquals(emptyList, actualList);
    }

    @Test
    public void getAllByAccountId_provideIncorrectId_throwEntityNotFoundException() {
        long incorrectId = -1L;
        Mockito.when(accountService.getById(incorrectId))
            .thenThrow(new EntityNotFoundException(incorrectId));

        Assertions.assertThrows(EntityNotFoundException.class, () ->
            bidService.getAllByAccountId(incorrectId)
        );
        Mockito.verify(accountService).getById(incorrectId);
    }

    @Test
    public void save_provideCorrectDto_saveSuccessfully() {
        BidDto testDto = new BidDto(
            testBid.getAccountId(),
            testBid.getStockName(),
            testBid.getBidPrice(),
            testBid.getStockQuantity());
        long expectedId = testBid.getId();
        Mockito.when(repo.save(testDto)).thenReturn(expectedId);

        long actualId = bidService.save(testDto);

        Assertions.assertEquals(expectedId, actualId);
        Mockito.verify(accountService).getById(testBid.getAccountId());
    }

    @Test
    public void save_provideDtoWithIncorrectAccountId_throwEntityNotFoundException() {
        long incorrectAccountId = -1L;
        BidDto dtoWithIncorrectAccountId = new BidDto(
            incorrectAccountId, "NONE",
            BigDecimal.ZERO, 0);

        Mockito.when(accountService.getById(incorrectAccountId))
            .thenThrow(new EntityNotFoundException(incorrectAccountId));

        Assertions.assertThrows(EntityNotFoundException.class, () ->
            bidService.save(dtoWithIncorrectAccountId));
    }

    private Bid getTestBid() {
        return Bid.builder()
            .id(13L)
            .accountId(42L)
            .stockName("STCK")
            .bidPrice(BigDecimal.TEN)
            .stockQuantity(1)
            .build();
    }
}