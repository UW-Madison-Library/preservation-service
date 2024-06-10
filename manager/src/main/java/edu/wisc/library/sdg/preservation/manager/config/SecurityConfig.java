package edu.wisc.library.sdg.preservation.manager.config;

import edu.wisc.library.sdg.preservation.manager.auth.ApiKeyAuthFilter;
import edu.wisc.library.sdg.preservation.manager.auth.ApiKeyAuthManager;
import edu.wisc.library.sdg.preservation.manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.servlet.Filter;
import java.util.Map;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    public static final String INTERNAL_API_AUTHORITY = "INT_API";
    public static final String PUBLIC_API_AUTHORITY = "PUB_API";
    public static final String ACTUATOR_AUTHORITY = "ACTUCATOR";

    @Autowired
    private UserService userService;

    @Value("${app.auth.worker.username}")
    private String workerUsername;

    @Value("${app.auth.worker.password}")
    private String workerPassword;

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
                // Disable CSRF
                .csrf().disable()
                // Add the API Key filter used for the public API endpoint
                .addFilter(apiKeyAuthFilter())
                // Add the Basic Auth filter used for the internal API endpoint
                .httpBasic().realmName("preservation").and()
                // Auth for public API
                .authorizeRequests().antMatchers("/api/**").hasAuthority(PUBLIC_API_AUTHORITY)
                .and()
                .authorizeRequests().antMatchers("/actuator/prometheus").permitAll()
                .and()
                .authorizeRequests().antMatchers("/actuator/**").hasAuthority(ACTUATOR_AUTHORITY)
                .and()
                // Auth for internal API
                .authorizeRequests().antMatchers("/internal/api/**").hasAuthority(INTERNAL_API_AUTHORITY);

        return httpSecurity.build();
    }

    @Bean
    public Filter apiKeyAuthFilter() {
        var filter = new ApiKeyAuthFilter();
        filter.setAuthenticationManager(new ApiKeyAuthManager(userService));
        return filter;
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        var userManager = new InMemoryUserDetailsManager();

        userManager.createUser(User.withUsername(workerUsername)
                .roles("ADMIN")
                .authorities(INTERNAL_API_AUTHORITY)
                .password(workerPassword)
                .build());

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

}
