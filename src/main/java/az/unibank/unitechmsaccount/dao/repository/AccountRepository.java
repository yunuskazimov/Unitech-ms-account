package az.unibank.unitechmsaccount.dao.repository;

import az.unibank.unitechmsaccount.dao.entity.AccountEntity;
import az.unibank.unitechmsaccount.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends PagingAndSortingRepository<AccountEntity, Long> {
    Optional<Page<AccountEntity>> findAllByCustomerPinAndStatus(String customerPin,
                                                                Status status,
                                                                Pageable pageable);

    Optional<Page<AccountEntity>> findAllByStatus(Status status, Pageable pageable);

    Optional<AccountEntity> findByAccountId(String accountId);
}
