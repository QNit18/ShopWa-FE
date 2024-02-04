package com.shopwa.security;

import com.shopwa.security.oauth.CustomerOAuth2UserService;
import com.shopwa.security.oauth.OAuth2LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Autowired private CustomerOAuth2UserService oAuth2UserService;
    @Autowired private OAuth2LoginSuccessHandler oAuth2LoginHandler;
    @Autowired private DatabaseLoginSuccessHandler databseLoginHandler;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain configureHttpSecurity(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/account_details", "/update_account_details", "/orders/**",
                        "/cart", "/address_book/**", "/checkout", "/place_order", "/process_paypal_order",
                        "/reviews/**","/write_review/**", "/post_review",
                        "/ask_question/**", "/post_question/**", "/customer/questions/**").authenticated()
                .anyRequest().permitAll()
                )
                .formLogin(form -> form
                    .loginPage("/login")
                    .usernameParameter("email")
                    .successHandler(databseLoginHandler)
                    .permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                    .loginPage("/login")
                    .userInfoEndpoint(u -> u.userService(oAuth2UserService))
                    .successHandler(oAuth2LoginHandler)
                )
                .logout(logout -> logout.permitAll())
                .rememberMe(rmb -> rmb
                    .key("1234567890_aBcDeFjhijklmnopqrstuwxyz")
                    .tokenValiditySeconds(12 * 24 * 60 * 60)
                 )
                .sessionManagement(seg -> seg.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
        ;
        return http.build();
    }

    @Bean
    WebSecurityCustomizer configureWebSecurity() throws Exception {
        return (web) -> web.ignoring().requestMatchers("/images/**", "/js/**", "/webjars/**");
    }


    @Bean
    UserDetailsService userDetailsService(){
        return new CustomerUserDetailService();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
}
