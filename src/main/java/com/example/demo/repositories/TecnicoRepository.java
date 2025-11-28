package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.Tecnico;




public interface TecnicoRepository extends JpaRepository<Tecnico, Integer> {

}
