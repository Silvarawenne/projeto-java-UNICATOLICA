package com.example.demo.services.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.domain.num.Perfil; // Seu enum de Perfil

public class UserDetailsServiceImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String email;
    private String senha;
    private Collection<? extends GrantedAuthority> authorities; 

    // CORREÇÃO FINAL: Garante que o prefixo ROLE_ (vindo do Enum Perfil.java) seja a autoridade
    public UserDetailsServiceImpl(Integer id, String email, String senha, Set<Perfil> perfis) {
        super();
        this.id = id;
        this.email = email;
        this.senha = senha;
        
        // Mapeia os Perfis (que já são "ROLE_ADMIN") para SimpleGrantedAuthority.
        // Se seu Enum retorna "ROLE_ADMIN", esta linha está perfeita.
        this.authorities = perfis.stream()
                                 .map(x -> new SimpleGrantedAuthority(x.getDescricao()))
                                 .collect(Collectors.toSet());
    }

    public Integer getId() {
        return id;
    }
    // ... (O restante dos métodos padrões (getPassword, getUsername, isEnabled, etc.) permanece inalterado)
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }
    
    // ... (o restante dos métodos padrão)
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}