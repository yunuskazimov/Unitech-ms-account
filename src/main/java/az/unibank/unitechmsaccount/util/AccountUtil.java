package az.unibank.unitechmsaccount.util;

import az.unibank.unitechmsaccount.dao.entity.AccountEntity;
import az.unibank.unitechmsaccount.dao.repository.AccountRepository;
import az.unibank.unitechmsaccount.model.Status;
import az.unibank.unitechmsaccount.model.exception.AccountNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccountUtil {
    private final AccountRepository repo;

    public Page<AccountEntity> findAllByStatusIsActive(Pageable pageable) {
        log.info("service util findAllByStatusIsActive started");
        Page<AccountEntity> entityList = repo.findAllByStatus(Status.ACTIVE, pageable).get();
        if (entityList.isEmpty()) {
            log.info("service util findAllByStatusIsActive run exception");
            throw new AccountNotFoundException("Account Not Found");
        } else {
            log.info("service util findAllByStatusIsActive completed");
            return entityList;
        }
    }

    public Page<AccountEntity> findAllByCustomerIdAndStatusIsActive(String customerPin,
                                                                    Pageable pageable) {
        log.info("service util findAllByCustomerIdAndStatusIsActive started");
        Page<AccountEntity> entityList = repo
                .findAllByCustomerPinAndStatus(customerPin, Status.ACTIVE, pageable).get();
        if (entityList.isEmpty()) {
            log.info("service util findAllByCustomerIdAndStatusIsActive run exception");
            throw new AccountNotFoundException("Account Not Found");
        } else {
            log.info("service util findAllByCustomerIdAndStatusIsActive completed");
            return entityList;
        }
    }

    public AccountEntity findById(String uuid) {
        log.info("service util findById started");
        AccountEntity entity = repo.findByAccountId(uuid).orElseThrow(
                () -> new AccountNotFoundException("Account Not Found"));
        log.info("service util findById completed");
        return entity;

    }
}
