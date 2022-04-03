package az.unibank.unitechmsaccount.service.impl;

import az.unibank.unitechmsaccount.dao.entity.AccountEntity;
import az.unibank.unitechmsaccount.dao.repository.AccountRepository;
import az.unibank.unitechmsaccount.mapper.AccountMapper;
import az.unibank.unitechmsaccount.model.AccountCreateDto;
import az.unibank.unitechmsaccount.model.AccountDto;
import az.unibank.unitechmsaccount.model.PageDto;
import az.unibank.unitechmsaccount.model.Status;
import az.unibank.unitechmsaccount.service.AccountService;
import az.unibank.unitechmsaccount.util.AccountUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepo;
    private final AccountMapper accountMapper;
    private final AccountUtil accountUtil;

    @Override
    public AccountDto createAccount(String customerPin, AccountCreateDto createDto) {
        log.info("service createAccount started with customer PIN: {}", customerPin);
        AccountDto accountDto = accountMapper.toAccountDto(createDto);
        accountDto.setCustomerPin(customerPin);
        accountDto.setStatus(Status.ACTIVE);
        AccountEntity entity = accountRepo.save(accountMapper.toEntity(accountDto));
        log.info("service createAccount completed with customer PIN: {}", customerPin);
        return accountMapper.toDto(entity);
    }

    @Override
    public Page<AccountDto> getAccountsByCustomerId(String customerPin, PageDto page) {
        log.info("service getAccountsByCustomerId started by Customer PIN: {}", customerPin);
        return accountUtil
                .findAllByCustomerIdAndStatusIsActive(customerPin, getPageable(page))
                .map(accountMapper::toDto);
    }

    @Override         //only admin have access when add permission ms
    public Page<AccountDto> getAccounts(PageDto page) {
        log.info("service getAccounts started");
        return accountUtil.findAllByStatusIsActive(getPageable(page))
                .map(accountMapper::toDto);
    }

    @Override
    public AccountDto getAccountById(String uuid) {
        log.info("service getAccountById started by UUID: {}", uuid);
        return accountMapper.toDto(accountUtil.findById(uuid));
    }

    @Override
    public AccountDto updateAccount(AccountDto dto) {
        log.info("service updateAccount started with customer PIN: {}", dto.getCustomerPin());
        AccountEntity entity = accountRepo.save(accountMapper.toEntity(dto));
        log.info("service updateAccount completed with customer PIN: {}", dto.getCustomerPin());
        return accountMapper.toDto(entity);
    }

    private Pageable getPageable(PageDto page) {
        Sort sort = Sort.by(page.getSortDirection(), page.getSortBy());
        return PageRequest.of(page.getPageNumber(), page.getPageSize(), sort);
    }
}
