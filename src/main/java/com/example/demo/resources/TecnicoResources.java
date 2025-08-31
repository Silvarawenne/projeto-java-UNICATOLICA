package com.example.demo.resources;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domai.Tecnico;
import com.example.demo.domai.dtos.TecnicoDTO;
import com.example.demo.services.TecnicoService;

@RestController

@RequestMapping(value = "/tecnicos")

public class TecnicoResources {
	
	@Autowired
	private TecnicoService service;
	
	
	
	@GetMapping 
	public ResponseEntity<List<TecnicoDTO>> findAll(){
		List<Tecnico> list = service.findAll();
		List<TecnicoDTO> listDTO = list.stream()
				.map(obj -> new TecnicoDTO(obj))
				.collect(Collectors.toList());
		return ResponseEntity.ok().body(listDTO);
	}

}
