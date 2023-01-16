package com.touchblankspot.inventory.portal.security.config;

import static com.touchblankspot.inventory.portal.web.WebConstant.LOGIN_FAIL_ENDPOINT;
import static com.touchblankspot.inventory.portal.web.WebConstant.LOGIN_PAGE_ENDPOINT;
import static com.touchblankspot.inventory.portal.web.WebConstant.PERMIT_ALL_URL;

import com.touchblankspot.inventory.portal.security.handler.AppAuthenticationSuccessHandler;
import com.touchblankspot.inventory.portal.security.service.CustomUserDetailsService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class SecurityConfig {

  @Value("${encryption.remember_me.key}")
  private String rememberMeKey;

  @Value("${encryption.remember_me.token.validity}")
  private Integer rememberMeTokenValidity;

  @NonNull private final CustomUserDetailsService userDetailsService;

  @NonNull private final AppAuthenticationSuccessHandler successHandler;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf()
        .and()
        .authorizeHttpRequests(
            (requests) ->
                requests.requestMatchers(PERMIT_ALL_URL).permitAll().anyRequest().authenticated())
        .formLogin(
            form ->
                form.loginPage(LOGIN_PAGE_ENDPOINT)
                    .permitAll()
                    .successHandler(successHandler)
                    .failureUrl(LOGIN_FAIL_ENDPOINT))
        .rememberMe()
        .key(rememberMeKey)
        .tokenValiditySeconds(rememberMeTokenValidity)
        .and()
        .logout(logout -> logout.permitAll().deleteCookies("JSESSIONID"));
    http.headers().frameOptions().sameOrigin();
    return http.build();
  }

  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider(PasswordEncoder passwordEncoder) {
    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
    daoAuthenticationProvider.setUserDetailsService(userDetailsService);
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
    return daoAuthenticationProvider;
  }

  @Bean
  public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration)
      throws Exception {
    return configuration.getAuthenticationManager();
  }
}
