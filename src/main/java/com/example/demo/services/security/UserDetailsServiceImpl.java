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
    private PessoaRepository pessoaRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        // 1. Buscamos a pessoa no banco. Se não achar, ele já lança o erro aqui.
        Pessoa pessoa = pessoaRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + email));

        // 2. SE chegou aqui, é porque achou o usuário. Vamos imprimir o que ele achou:
        System.out.println("===============================================");
        System.out.println("=== DEBUG LOGIN (UserDetailsServiceImpl) ===");
        System.out.println("Email digitado: " + email);
        System.out.println("Email vindo do banco: " + pessoa.getEmail());
        System.out.println("Senha vinda do banco: [" + pessoa.getSenha() + "]"); // Coloquei colchetes para ver se tem espaço
        System.out.println("Perfis carregados: " + pessoa.getPerfis());
        System.out.println("===============================================");

        // 3. Retorna o objeto para o Spring Security verificar a senha
        return new UserSS(pessoa.getId(), pessoa.getEmail(), pessoa.getSenha(), pessoa.getPerfis());
    }
}