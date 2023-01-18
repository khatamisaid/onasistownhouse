package com.dreamtown.onasistownhouse.config;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.dreamtown.onasistownhouse.entity.User;
import com.dreamtown.onasistownhouse.repository.UserRepository;

@Component
public class CustomAuthProvider implements AuthenticationProvider {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpSession httpSession;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        User userTemp = new User();
        userTemp.setUsername(username);
        User user = new User();
        try{
            user = userRepository.findOne(Example.of(userTemp)).get();
        }catch(NoSuchElementException e){
            throw new BadCredentialsException("Username Tidak ditemukan");
        }
        if (encoder.matches(password, user.getPassword())) {
            httpSession.setAttribute("id", user.getIdUser());
            httpSession.setAttribute("username", user.getUsername());
            httpSession.setAttribute("role", user.getRole().getRoleName());
        } else {
            throw new BadCredentialsException("Username/Password Salah");
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList();
        grantedAuthorities.add(new SimpleGrantedAuthority((String) httpSession.getAttribute("role")));
        return new UsernamePasswordAuthenticationToken(username, password, grantedAuthorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

}
