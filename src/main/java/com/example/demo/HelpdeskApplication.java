package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class HelpdeskApplication implements CommandLineRunner { 

    public static void main(String[] args) {
        SpringApplication.run(HelpdeskApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        System.out.println("****************************************************");
        System.out.println("* APLICAÇÃO HELP DESK INICIADA COM SUCESSO!        *");
        System.out.println("****************************************************");
    }
}