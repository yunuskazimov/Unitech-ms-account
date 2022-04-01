package az.unibank.unitechmsaccount.service;

import az.unibank.unitechmsaccount.model.AccountCreateDto;
import az.unibank.unitechmsaccount.model.AccountDto;
import az.unibank.unitechmsaccount.model.PageDto;
import org.springframework.data.domain.Page;

public interface AccountService {

    AccountDto createAccount(String customerPin, AccountCreateDto accountCreateDto);

    Page<AccountDto> getAccountsByCustomerId(String customerPin, PageDto page);

    Page<AccountDto> getAccounts(PageDto page);

    AccountDto getAccountById(String uuid);

    AccountDto updateAccount(AccountDto dto);
}
