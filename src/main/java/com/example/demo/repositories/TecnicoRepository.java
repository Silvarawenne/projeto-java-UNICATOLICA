package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.domai.Tecnico;




public interface TecnicoRepository extends JpaRepository<Tecnico, Integer> {

}
