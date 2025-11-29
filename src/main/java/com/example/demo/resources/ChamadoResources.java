package com.example.demo.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; 
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.domain.Chamado;
import com.example.demo.domain.dtos.ChamadoDTO;
import com.example.demo.services.ChamadoService;

@RestController
@RequestMapping(value = "/chamados")
public class ChamadoResources {
	
	@Autowired
	private ChamadoService service;
	
	
	@PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')") 
	@GetMapping(value="/{id}")
	public ResponseEntity<ChamadoDTO> findById(@PathVariable Integer id){
		Chamado obj = service.findById(id);
		return ResponseEntity.ok().body(new ChamadoDTO(obj));
	}
	

	@PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')") 
	@GetMapping
	public ResponseEntity<List<ChamadoDTO>> findAll(){
		List<Chamado> list = service.findAll();
		List<ChamadoDTO> listDTO = list.stream()
									  .map(obj -> new ChamadoDTO(obj))
									  .collect(Collectors.toList());
		return ResponseEntity.ok().body(listDTO);
	}
	
	
	@PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE', 'TECNICO')") // ⚠️ CORREÇÃO!
	@PostMapping
	public ResponseEntity<ChamadoDTO> create (@Valid @RequestBody ChamadoDTO objDTO){
		Chamado obj = service.create(objDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(obj.getId())
				.toUri();
		return ResponseEntity.created(uri).build();
	}
	

	@PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')") 
	@PutMapping(value = "/{id}")
	public ResponseEntity<ChamadoDTO> update(@PathVariable Integer id, @Valid @RequestBody ChamadoDTO objDTO){
		Chamado newOBJ = service.update(id, objDTO);
		return ResponseEntity.ok().body(new ChamadoDTO(newOBJ));
	}
	

	@PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')") 
	@DeleteMapping (value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}