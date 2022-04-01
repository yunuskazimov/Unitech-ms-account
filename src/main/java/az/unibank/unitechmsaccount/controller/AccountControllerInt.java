package az.unibank.unitechmsaccount.controller;

import az.unibank.unitechmsaccount.model.AccountDto;
import az.unibank.unitechmsaccount.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/int/api/account")
@Slf4j
@RequiredArgsConstructor
public class AccountControllerInt {
    private final AccountService service;

    @GetMapping("/id/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public AccountDto getAccountsByUUID(@PathVariable String uuid) {
        log.info("controller int getAccountsByUUID started");
        return service.getAccountById(uuid);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.ACCEPTED)
    public AccountDto updateAccount(@RequestBody AccountDto dto) {
        log.info("controller int updateAccount started");
        return service.updateAccount(dto);
    }

}
