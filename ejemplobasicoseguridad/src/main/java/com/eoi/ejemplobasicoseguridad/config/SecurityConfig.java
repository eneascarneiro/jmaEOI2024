package com.eoi.ejemplobasicoseguridad.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
@Configuration
public class SecurityConfig {

    @Autowired
    private UserDetailsService uds;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/welcome",true)

                        .permitAll()
                );
        http.logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        /*.logoutSuccessHandler(logoutSuccessHandler)
                        .invalidateHttpSession(true)
                        .addLogoutHandler(logoutHandler)
                        .deleteCookies(cookieNamesToClear)*/
                );
        http.authorizeHttpRequests()
                .requestMatchers("/home","/register","/saveUser","/").permitAll()
                .requestMatchers("/admin").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/mgr").hasAuthority("ROLE_MANAGER")
                .requestMatchers("/emp").hasAuthority("ROLE_EMPLOYEE")
                .requestMatchers("/hr").hasAuthority("ROLE_HR")
                .requestMatchers("/common").hasAnyAuthority("ROLE_EMPLOYEE,ROLE_MANAGER,ROLE_ADMIN")
                .anyRequest().authenticated()

                .and()
                .exceptionHandling()
                .accessDeniedPage("/accessDenied")

                .and()
                .authenticationProvider(authenticationProvider());

        return http.build();

    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(uds);
        authenticationProvider.setPasswordEncoder(encoder);
        return authenticationProvider;
    }
    @Bean
    static GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("ROLE_");
    }
}