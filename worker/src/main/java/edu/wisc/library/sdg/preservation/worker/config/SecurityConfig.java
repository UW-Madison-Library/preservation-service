package edu.wisc.library.sdg.preservation.worker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import java.util.Map;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    public static final String ACTUATOR_AUTHORITY = "ACTUCATOR";

    @Value("${app.ssl.enabled}")
    private boolean sslEnabled;

    @Value("#{${app.auth.actuator.users} ?: {T(java.util.Collections).emptyMap()}}")
    private Map<String, String> actuatorUsers;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        if (sslEnabled) {
            httpSecurity = httpSecurity.requiresChannel().anyRequest().requiresSecure().and();
        }

        httpSecurity
                .exceptionHandling().authenticationEntryPoint(entryPoint())
                // Disable CSRF
                .and().csrf().disable()
                // Enable Basic Auth
                .httpBasic().authenticationEntryPoint(entryPoint())
                // Configure paths
                .and().authorizeRequests().antMatchers("/actuator/prometheus").permitAll()
                .and().authorizeRequests().antMatchers("/actuator/**").hasAuthority(ACTUATOR_AUTHORITY);
        return httpSecurity.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        var userManager = new InMemoryUserDetailsManager();

        actuatorUsers.forEach((username, password) -> {
            userManager.createUser(User.withUsername(username)
                    .roles("ADMIN")
                    .authorities(ACTUATOR_AUTHORITY)
                    .password(password)
                    .build());
        });

        return userManager;
    }

    /**
     * Spring Security uses DelegatingPasswordEncoder by default. However, we are customizing this by exposing
     * PasswordEncoder as a Spring bean. If you want to use the default encoder, then add the id
     * (such as `{bcrypt}`) to the encrypted user passwords in the properties files and remove this bean.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationEntryPoint entryPoint() {
        var entryPoint = new BasicAuthenticationEntryPoint();
        entryPoint.setRealmName("preservation");
        return entryPoint;
    }

}
