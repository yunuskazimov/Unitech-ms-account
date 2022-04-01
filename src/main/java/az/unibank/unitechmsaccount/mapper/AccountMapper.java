package az.unibank.unitechmsaccount.mapper;

import az.unibank.unitechmsaccount.dao.entity.AccountEntity;
import az.unibank.unitechmsaccount.model.AccountCreateDto;
import az.unibank.unitechmsaccount.model.AccountDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    AccountDto toDto(AccountEntity entity);

    AccountEntity toEntity(AccountDto dto);

    AccountDto toAccountDto(AccountCreateDto createDto);

}
