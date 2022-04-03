package az.unibank.unitechmsaccount.service.impl;

import az.unibank.unitechmsaccount.dao.entity.AccountEntity;
import az.unibank.unitechmsaccount.dao.repository.AccountRepository;
import az.unibank.unitechmsaccount.mapper.AccountMapper;
import az.unibank.unitechmsaccount.model.AccountCreateDto;
import az.unibank.unitechmsaccount.model.AccountDto;
import az.unibank.unitechmsaccount.model.CurrencyType;
import az.unibank.unitechmsaccount.model.PageDto;
import az.unibank.unitechmsaccount.model.Status;
import az.unibank.unitechmsaccount.model.exception.AccountNotFoundException;
import az.unibank.unitechmsaccount.model.exception.ErrorCodes;
import az.unibank.unitechmsaccount.util.AccountUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    private static final String CUSTOMER_PIN = "1OYG677";
    private static final String ACCOUNT_ID = "dda8850c-5bff-4ee3-a118-9684e2fe004d";
    private static final BigDecimal AMOUNT = BigDecimal.valueOf(100);
    private static final BigDecimal AMOUNT2 = BigDecimal.valueOf(150);

    @Mock
    private AccountRepository accountRepo;
    @Mock
    private AccountMapper accountMapper;
    @Mock
    private AccountUtil accountUtil;
    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        accountService = new AccountServiceImpl(accountRepo, accountMapper, accountUtil);
    }

    @Test
    void createAccount() {
        //given
        AccountCreateDto createDto = new AccountCreateDto(AMOUNT, CurrencyType.AZN);
        AccountDto dto = getAccountDto();
        AccountEntity entity = getAccountEntity();

        //when
        when(accountMapper.toAccountDto(createDto)).thenReturn(dto);
        when(accountMapper.toEntity(dto)).thenReturn(entity);
        when(accountRepo.save(entity)).thenReturn(entity);
        when(accountMapper.toDto(entity)).thenReturn(dto);

        var actual = accountService.createAccount(CUSTOMER_PIN, createDto);

        assertEquals(actual, dto);

        verify(accountRepo, times(1)).save(entity);

    }

    @Test
    void getAccountsByCustomerId() {
        //given
        PageDto pageDto = getPageDto();

        AccountEntity entity = getAccountEntity();
        Page<AccountEntity> entityList = new PageImpl<>(List.of(entity));

        AccountDto dto = getAccountDto();
        Page<AccountDto> dtoList = new PageImpl<>(List.of(dto));

        //when
        when(accountUtil.findAllByCustomerIdAndStatusIsActive(CUSTOMER_PIN, getPageable(pageDto)))
                .thenReturn(entityList);
        when(accountMapper.toDto(entity)).thenReturn(dto);


        var actual = accountService.getAccountsByCustomerId(CUSTOMER_PIN, pageDto);

        assertEquals(actual, dtoList);

        verify(accountUtil, times(1))
                .findAllByCustomerIdAndStatusIsActive(CUSTOMER_PIN, getPageable(pageDto));

    }

    @Test
    void getAccounts() {
        //given
        PageDto pageDto = getPageDto();

        AccountEntity entity = getAccountEntity();
        Page<AccountEntity> entityList = new PageImpl<>(List.of(entity));

        AccountDto dto = getAccountDto();
        Page<AccountDto> dtoList = new PageImpl<>(List.of(dto));

        //when
        when(accountUtil.findAllByStatusIsActive(getPageable(pageDto))).thenReturn(entityList);
        when(accountMapper.toDto(entity)).thenReturn(dto);

        var actual = accountService.getAccounts(pageDto);

        assertEquals(actual, dtoList);

        verify(accountUtil, times(1))
                .findAllByStatusIsActive(getPageable(pageDto));

    }

    @Test
    void updateAccount() {
        // given
        AccountDto newDto = getAccountDto();
        newDto.setAmount(AMOUNT2);
        AccountEntity entity = getAccountEntity();
        AccountEntity newEntity = getAccountEntity();
        newEntity.setAmount(AMOUNT2);

        //when
        when(accountMapper.toEntity(newDto)).thenReturn(newEntity);
        when(accountRepo.save(newEntity)).thenReturn(newEntity);
        when(accountMapper.toDto(newEntity)).thenReturn(newDto);

        var actual = accountService.updateAccount(newDto);

        assertEquals(actual, newDto);
        verify(accountRepo, times(1)).save(newEntity);
    }

    @Test
    void getAccountById() {
        //given
        AccountEntity entity = getAccountEntity();
        AccountDto dto = getAccountDto();

        //when
        when(accountUtil.findById(ACCOUNT_ID)).thenReturn(entity);
        when(accountMapper.toDto(entity)).thenReturn(dto);

        var actual = accountService.getAccountById(ACCOUNT_ID);

        assertEquals(actual, dto);
        verify(accountUtil, times(1)).findById(ACCOUNT_ID);

    }

    @Test
    void getAccountById_AccountNotFound_ShouldThrowAccountNotFoundException() {

        //when
        when(accountUtil.findById(ACCOUNT_ID))
                .thenThrow(AccountNotFoundException
                        .of(ErrorCodes.NOT_FOUND, "Account Not Found"));

        var actual = assertThrows(AccountNotFoundException.class,
                () -> accountService.getAccountById(ACCOUNT_ID));

        Assertions.assertEquals(ErrorCodes.NOT_FOUND, actual.getCode());

        verify(accountUtil, times(1)).findById(ACCOUNT_ID);


    }

    private AccountEntity getAccountEntity() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountId(ACCOUNT_ID);
        accountEntity.setCustomerPin(CUSTOMER_PIN);
        accountEntity.setAmount(AMOUNT);
        accountEntity.setCurrencyType(CurrencyType.AZN);
        accountEntity.setStatus(Status.ACTIVE);
        accountEntity.setCreatedAt(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        accountEntity.setUpdatedAt(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        return accountEntity;
    }

    private AccountDto getAccountDto() {
        AccountDto accountDto = new AccountDto(ACCOUNT_ID, CUSTOMER_PIN,
                AMOUNT, CurrencyType.AZN, Status.ACTIVE);
        return accountDto;
    }

    private PageDto getPageDto() {
        PageDto page = new PageDto();
        page.setPageNumber(5);
        page.setPageSize(5);
        page.setSortDirection(Sort.Direction.ASC);
        page.setSortBy("sortBy");
        return page;
    }

    private Pageable getPageable(PageDto page) {
        Sort sort = Sort.by(page.getSortDirection(), page.getSortBy());
        return PageRequest.of(page.getPageNumber(), page.getPageSize(), sort);
    }
}
