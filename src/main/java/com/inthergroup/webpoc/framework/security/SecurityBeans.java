//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.security;

import com.inthergroup.webpoc.framework.config.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

/**
 * @author gvandenbekerom
 * @since 16-Nov-18
 */
@Configuration
public class SecurityBeans {
    /**
     * @return Password encoder to decode passwords of user datasource with, StandardPasswordEncoder is deprecated, but it's used in intherlc-core
     */
    @Bean
    public PasswordEncoder encoder() {
        return new StandardPasswordEncoder(Constants.PASSWORD_SALT);
    }
}
