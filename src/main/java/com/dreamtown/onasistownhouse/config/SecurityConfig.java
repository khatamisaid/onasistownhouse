package com.dreamtown.onasistownhouse.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthProvider customAuthProvider;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/assets/**")
                .antMatchers("/css/**")
                .antMatchers("/icons/**")
                .antMatchers("/img/**")
                .antMatchers("/js/**")
                .antMatchers("/fonts/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthProvider);
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // http.csrf().and().headers().xssProtection().and().frameOptions().sameOrigin().and()
        http.csrf().disable()
                .headers().frameOptions().sameOrigin().and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/admin/**").authenticated()
                .and()
                .formLogin()
                .failureUrl("/login?error")
                .loginPage("/login")
                .and()
                .logout().deleteCookies("JSESSIONID")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .and()
                .sessionManagement()
                .sessionFixation().migrateSession()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .maximumSessions(10)
                .expiredUrl("/login?expired")
                .and()
                .invalidSessionUrl("/login");
        http.requiresChannel((channel) -> channel.anyRequest().requiresSecure());
    }
}
