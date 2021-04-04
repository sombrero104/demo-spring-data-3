package me.demospringdata3.post;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountAuditAware implements AuditorAware<Account> {

    /**
     * 
     */


    @Override
    public Optional<Account> getCurrentAuditor() {
        return Optional.empty();
    }

}
