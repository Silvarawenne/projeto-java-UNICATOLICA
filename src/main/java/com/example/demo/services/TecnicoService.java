package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domai.Tecnico;
import com.example.demo.repositories.TecnicoRepository;
import com.example.demo.services.exceptions.ObjectNotFoundException;

@Service
public class TecnicoService {
	
	
	@Autowired
	private TecnicoRepository repository;
	
	public Tecnico findById(Integer id) {
		
		Optional<Tecnico> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: "+id));
		
	}
	
	public List<Tecnico> findAll(){
		return repository.findAll();
	}
	

}
