//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @author gvandenbekerom
 * @since 03-Oct-18
 *
 * Used to configure the jdbcTemplate's for native queries on non-framework databases
 */
@Configuration
public class DbConfig {

    @Value("${data.datasource.url}")
    private String dataUrl;
    @Value("${data.datasource.username}")
    private String dataUsername;
    @Value("${data.datasource.password}")
    private String dataPassword;

    @Primary
    @Bean(name = "data")
    public JdbcTemplate dataJdbcTemplate() {
        DataSource ds = DataSourceBuilder
                .create()
                .url(dataUrl)
                .username(dataUsername)
                .password(dataPassword)
                .build();
        return new JdbcTemplate(ds);
    }

    @Value("${user.datasource.url}")
    private String userUrl;
    @Value("${user.datasource.username}")
    private String userUsername;
    @Value("${user.datasource.password}")
    private String userPassword;

    @Bean(name = "user")
    public JdbcTemplate userJdbcTemplate() {
        DataSource ds = DataSourceBuilder
                .create()
                .url(userUrl)
                .username(userUsername)
                .password(userPassword)
                .build();
        return new JdbcTemplate(ds);
    }
}
