package az.unibank.unitechmsaccount.config;

import az.unibank.unitechmsaccount.mapper.AccountMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public AccountMapper accountMapper() {
        return AccountMapper.INSTANCE;
    }

}
