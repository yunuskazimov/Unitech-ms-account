package az.unibank.unitechmsaccount.util;

import az.unibank.unitechmsaccount.dao.entity.AccountEntity;
import az.unibank.unitechmsaccount.dao.repository.AccountRepository;
import az.unibank.unitechmsaccount.model.Status;
import az.unibank.unitechmsaccount.model.exception.AccountNotFoundException;
import az.unibank.unitechmsaccount.model.exception.ErrorCodes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccountUtil {
    private final AccountRepository repo;

    public Page<AccountEntity> findAllByStatusIsActive(Pageable pageable) {
        log.info("service util findAllByStatusIsActive started");
        Optional<Page<AccountEntity>> entityList = repo.findAllByStatus(Status.ACTIVE, pageable);
        if (entityList.isEmpty()) {
            log.info("service util findAllByStatusIsActive run exception");
            throw AccountNotFoundException.of(ErrorCodes.NOT_FOUND, "Account Not Found");
        } else {
            log.info("service util findAllByStatusIsActive completed");
            return entityList.get();
        }
    }

    public Page<AccountEntity> findAllByCustomerIdAndStatusIsActive(String customerPin,
                                                                    Pageable pageable) {
        log.info("service util findAllByCustomerIdAndStatusIsActive started");
        Optional<Page<AccountEntity>> entityList = repo
                .findAllByCustomerPinAndStatus(customerPin, Status.ACTIVE, pageable);
        if (entityList.isEmpty()) {
            log.info("service util findAllByCustomerIdAndStatusIsActive run exception");
            throw AccountNotFoundException.of(ErrorCodes.NOT_FOUND, "Account Not Found");
        } else {
            log.info("service util findAllByCustomerIdAndStatusIsActive completed");
            return entityList.get();
        }
    }

    public AccountEntity findById(String uuid) {
        log.info("service util findById started");
        AccountEntity entity = repo.findByAccountId(uuid).orElseThrow(
                () -> AccountNotFoundException.of(ErrorCodes.NOT_FOUND, "Account Not Found"));
        log.info("service util findById completed");
        return entity;

    }
}
