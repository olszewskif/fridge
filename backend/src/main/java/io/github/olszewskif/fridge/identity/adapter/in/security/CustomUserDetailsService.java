package io.github.olszewskif.fridge.identity.adapter.in.security;

import io.github.olszewskif.fridge.identity.application.dto.SecurityUserDto;
import io.github.olszewskif.fridge.identity.application.port.outbound.LoadSecurityUser;
import io.github.olszewskif.fridge.identity.domain.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final LoadSecurityUser loadSecurityUser;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        SecurityUserDto user = loadSecurityUser.findUserByEmail(new Email(email))
                .orElseThrow(() -> new UsernameNotFoundException("User " + email + "not found!"));
        return new SecurityUser(user);
    }
}
