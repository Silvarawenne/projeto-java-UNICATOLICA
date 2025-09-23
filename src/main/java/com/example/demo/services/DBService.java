package com.example.demo.services;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // NOVO IMPORT
import org.springframework.stereotype.Service;

import com.example.demo.domain.Chamado;
import com.example.demo.domain.Cliente;
import com.example.demo.domain.Tecnico;
import com.example.demo.domain.num.Perfil; 
import com.example.demo.domain.num.Prioridade;
import com.example.demo.domain.num.Status;
import com.example.demo.repositories.ChamadoRepository;
import com.example.demo.repositories.ClienteRepository;
import com.example.demo.repositories.TecnicoRepository;

@Service
public class DBService {

    @Autowired
    private TecnicoRepository tecnicoRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ChamadoRepository chamadoRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;

    public void instanciaDB() {
        // Criando instâncias dos objetos de exemplo

        // --- TECNICO ---
        // A SENHA AGORA É CRIPTOGRAFADA
        Tecnico tec1 = new Tecnico(null, "Bill Gates ADMIN", "06657796037", "bill.admin@email.com", encoder.encode("123"));
        tec1.addPerfil(Perfil.TECNICO); // Adiciona o perfil TECNICO
        tec1.addPerfil(Perfil.ADMIN);   // Adiciona o perfil ADMIN para testes

        // Um segundo técnico que será só TECNICO (para testar restrições)
        Tecnico tec2 = new Tecnico(null, "Linus Torvalds TEC", "99988877766", "linus.tec@email.com", encoder.encode("456"));
        tec2.addPerfil(Perfil.TECNICO); // Adiciona apenas o perfil TECNICO

        // --- CLIENTE ---
        // A SENHA AGORA É CRIPTOGRAFADA
        Cliente cli1 = new Cliente(null, "Steve Jobs CLIENTE", "70511744015", "steve.cliente@email.com", encoder.encode("789"));
        cli1.addPerfil(Perfil.CLIENTE); // Adiciona o perfil CLIENTE

        // --- CHAMADO ---
        // Salvando técnicos e clientes primeiro para que tenham IDs gerados
        tecnicoRepository.saveAll(Arrays.asList(tec1, tec2)); // Salva os dois técnicos
        clienteRepository.saveAll(Arrays.asList(cli1));

        
        Chamado cha1 = new Chamado(null, Prioridade.MEDIA, Status.ANDAMENTO, "Chamado 01", "Primeiro chamado do Steve", tec1, cli1);
        Chamado cha2 = new Chamado(null, Prioridade.ALTA, Status.ABERTO, "Chamado 02", "Problema com internet do Steve", null, cli1); // Chamado sem técnico atribuído

        // Salvando os chamados
        chamadoRepository.saveAll(Arrays.asList(cha1, cha2));
        
    }
}