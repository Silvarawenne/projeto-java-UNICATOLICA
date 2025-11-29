package com.example.demo.services.security;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService; // ⚠️ INTERFACE CORRIGIDA!
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Pessoa;
import com.example.demo.repositories.PessoaRepository; 
import com.example.demo.security.jwt.UserSS; // A classe que implementa UserDetails

@Service
@Primary // Prioriza este serviço sobre qualquer autoconfiguração
// A CLASSE DEVE IMPLEMENTAR UserDetailsService, NÃO UserDetails.
public class UserDetailsServiceImpl implements UserDetailsService { 
    
    // Injete o repositório para buscar a pessoa no banco de dados
    @Autowired
    private PessoaRepository repository; 

    // 🛑 CORREÇÃO CRÍTICA 1: CONSTRUTOR VAZIO (ZERO-ARGUMENTOS)
    // O Spring exige isso para instanciar o Bean @Service na inicialização.
    public UserDetailsServiceImpl() { 
        // Construtor padrão para o Spring Boot
    }
    
    // 🛑 CORREÇÃO CRÍTICA 2: IMPLEMENTAÇÃO DO MÉTODO OBRIGATÓRIO
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        // 1. Busca a pessoa pelo email no banco
        Pessoa pessoa = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado! E-mail: " + email));
        
        // 2. Converte a entidade Pessoa no objeto de segurança UserSS e o retorna
        // (Isso assume que a classe UserSS está correta e pronta para receber os dados)
        return new UserSS(
                pessoa.getId(), 
                pessoa.getEmail(), 
                pessoa.getSenha(), 
                pessoa.getPerfis()
        );
    }
}