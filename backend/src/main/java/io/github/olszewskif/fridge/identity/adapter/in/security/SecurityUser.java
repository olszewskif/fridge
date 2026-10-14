package io.github.olszewskif.fridge.identity.adapter.in.security;

import io.github.olszewskif.fridge.identity.application.dto.SecurityUserDto;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class SecurityUser implements UserDetails {

    private final SecurityUserDto userDto;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userDto.role().getName()));
    }

    @Override
    public @Nullable String getPassword() {
        return userDto.password();
    }

    @Override
    public String getUsername() {
        return userDto.email().value();
    }
}
