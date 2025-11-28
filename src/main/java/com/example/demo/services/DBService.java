package com.example.demo.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Pessoa;
import com.example.demo.domain.Tecnico;
import com.example.demo.domain.num.Perfil;
import com.example.demo.repositories.PessoaRepository;
import com.example.demo.repositories.TecnicoRepository;

@Service
public class DBService {

    @Autowired
    private TecnicoRepository tecnicoRepository;
    @Autowired
    private PessoaRepository pessoaRepository; // Para verificar se já existe
    @Autowired
    private BCryptPasswordEncoder encoder;

    public void instanciaDB() {
        System.out.println(">>> DB SERVICE: INICIANDO VERIFICAÇÃO DO ADMIN... <<<");

        String emailAdmin = "bill.admin@email.com";
        
        // 1. Verifica se o usuário já existe pelo email
        Optional<Pessoa> pessoaExistente = pessoaRepository.findByEmail(emailAdmin);

        if (pessoaExistente.isPresent()) {
            // CENÁRIO A: O usuário já existe! Vamos garantir que ele é ADMIN.
            System.out.println(">>> USUÁRIO " + emailAdmin + " JÁ EXISTE. ATUALIZANDO PERMISSÕES... <<<");
            
            // Como sabemos que criamos como Técnico, fazemos o Cast
            if(pessoaExistente.get() instanceof Tecnico) {
                Tecnico admin = (Tecnico) pessoaExistente.get();
                admin.addPerfil(Perfil.ADMIN); // Garante o perfil 0 (ADMIN)
                admin.setSenha(encoder.encode("123")); // Garante a senha certa
                tecnicoRepository.save(admin);
            }
            
        } else {
            // CENÁRIO B: O usuário não existe. Vamos criar do zero.
            System.out.println(">>> CRIANDO USUÁRIO ADMIN DO ZERO... <<<");
            
            Tecnico admin = new Tecnico(null, "Bill Gates ADMIN", "06657796037", emailAdmin, encoder.encode("123"));
            admin.addPerfil(Perfil.CLIENTE);
            admin.addPerfil(Perfil.ADMIN); // O Perfil Poderoso!
            
            tecnicoRepository.save(admin);
        }
        
        System.out.println(">>> DB SERVICE: PROCESSO CONCLUÍDO! <<<");
    }
}