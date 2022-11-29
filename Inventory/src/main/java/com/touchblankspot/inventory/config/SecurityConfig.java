package com.touchblankspot.inventory.config;

import static com.touchblankspot.inventory.web.WebConstant.LOGIN_FAIL_ENDPOINT;
import static com.touchblankspot.inventory.web.WebConstant.LOGIN_PAGE_ENDPOINT;
import static com.touchblankspot.inventory.web.WebConstant.LOGIN_SUCCESS_ENDPOINT;
import static com.touchblankspot.inventory.web.WebConstant.PERMIT_ALL_URL;

import com.touchblankspot.inventory.service.CustomUserDetailsService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class SecurityConfig {

  @Value("${encryption.remember_me.key:3fZVNzasApraF509fhHT}")
  private String rememberMeKey;

  @Value("${encryption.remember_me.token.validity:86400}")
  private Integer rememberMeTokenValidity;

  @NonNull private final CustomUserDetailsService userDetailsService;

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
                    .defaultSuccessUrl(LOGIN_SUCCESS_ENDPOINT)
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
