package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired; // Importante
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Importante

@SpringBootApplication(exclude = {
	    // Excluímos este fallback para que o Spring use sua implementação customizada (JPA)
	    UserDetailsServiceAutoConfiguration.class 
	})
public class HelpdeskApplication implements CommandLineRunner {

    // INJETAMOS O ENCODER AQUI
    @Autowired
    private BCryptPasswordEncoder encoder;

    public static void main(String[] args) {
        SpringApplication.run(HelpdeskApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("****************************************************");
        System.out.println("* APLICAÇÃO HELP DESK INICIADA COM SUCESSO!        *");
        System.out.println("****************************************************");
        
        // AQUI ESTÁ A MÁGICA: GERANDO A SENHA
        System.out.println("SENHA CRIPTOGRAFADA PARA 123: " + encoder.encode("123"));
    }
}