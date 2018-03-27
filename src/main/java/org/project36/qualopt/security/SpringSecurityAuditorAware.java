package org.project36.qualopt.security;

import org.project36.qualopt.config.Constants;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Implementation of AuditorAware based on Spring Security.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        String userLogin = SecurityUtils.getCurrentUserLogin();

        if (userLogin == null) {
            userLogin = Constants.SYSTEM_ACCOUNT;
        }

        Optional<String> oUserLogin = Optional.ofNullable(userLogin);

        return oUserLogin;
    }
}
