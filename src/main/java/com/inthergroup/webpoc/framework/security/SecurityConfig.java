//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.inthergroup.webpoc.framework.security.jwt.JwtAuthenticationFilter;
import com.inthergroup.webpoc.framework.security.jwt.JwtAuthorizationFilter;
import com.inthergroup.webpoc.framework.security.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * @author gvandenbekerom
 * @since 16-Nov-18
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder encoder;
    private final UserDetailsService userDetailsService; // TODO: implement custom that works with external datasource
    private final JwtService jwtService;

    @Autowired
    public SecurityConfig(PasswordEncoder encoder, UserDetailsService userDetailsService, JwtService jwtService) {
        this.encoder = encoder;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    /**
     * Spring Security Configuration.
     *
     * Enables Cross-origin Resource Sharing (CORS) headers based on CorsConfiguration Bean below
     * Enables Cross-site Request Forgery (CSRF) security
     *
     * Exposes POST /login for authentication and token retrieval
     *
     * Configures which requests should be authorized by token and if a certain usergroup is needed for this access
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable()

                .authorizeRequests()

                .antMatchers(HttpMethod.GET, "/").permitAll()
                .antMatchers(HttpMethod.GET, "/*.js").permitAll()
                .antMatchers(HttpMethod.GET, "/*.css").permitAll()
                .antMatchers(HttpMethod.GET, "/*.jpg").permitAll()
                .antMatchers(HttpMethod.GET, "/*.png").permitAll()
                .antMatchers(HttpMethod.GET, "/favicon.ico").permitAll()
                .antMatchers(HttpMethod.GET,"/swagger-ui.html/**").permitAll()
                .antMatchers(HttpMethod.GET,"/actuator/**").permitAll()

                .antMatchers("/api/**").authenticated()

                // Add global role authentication checks here

                .and()
                .addFilterBefore(new JwtAuthenticationFilter(authenticationManager(), jwtService), BasicAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthorizationFilter(authenticationManager(), userDetailsService), BasicAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and().headers().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder);
    }

    /**
     * Used to configure allowed headers and origins for requests
     *
     * @return configuration concerning cross-origin request sharing used by Spring Security
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cors = new CorsConfiguration();
        cors.applyPermitDefaultValues();
        cors.addExposedHeader(HttpHeaders.AUTHORIZATION);

        UrlBasedCorsConfigurationSource conf = new UrlBasedCorsConfigurationSource();
        conf.registerCorsConfiguration("/**", cors);
        return conf;
    }

    @Bean
    public ObjectMapper securityGroupObjectMapper() {
        return new ObjectMapper().registerModule(
                new SimpleModule().setSerializerModifier(new BaseEntitySerializerModifier())
        );
    }
}
