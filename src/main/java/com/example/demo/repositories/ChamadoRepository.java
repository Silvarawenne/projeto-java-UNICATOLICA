package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.domai.Chamado;

public interface ChamadoRepository extends JpaRepository<Chamado, Integer> {

}
