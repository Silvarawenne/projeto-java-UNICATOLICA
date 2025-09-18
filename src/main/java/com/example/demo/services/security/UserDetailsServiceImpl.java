package com.example.demo.services.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Pessoa;
import com.example.demo.repositories.PessoaRepository;
import com.example.demo.security.jwt.UserSS;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PessoaRepository pessoaRepository; // Para buscar o usuário no banco

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Busca a Pessoa (Tecnico ou Cliente) pelo email
        Pessoa pessoa = pessoaRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + email));

        // Retorna um objeto UserSS (sua implementação de UserDetails)
        return new UserSS(pessoa.getId(), pessoa.getEmail(), pessoa.getSenha(), pessoa.getPerfis());
    }
}