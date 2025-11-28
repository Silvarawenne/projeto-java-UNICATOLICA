package com.example.demo.resources;
import java.util.List;
import java.util.stream.Collectors;
import java.net.URI;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.bind.annotation.DeleteMapping;

import com.example.demo.domain.Tecnico;
import com.example.demo.domain.dtos.TecnicoDTO;
import com.example.demo.services.TecnicoService;

@RestController
@RequestMapping(value = "/tecnicos")
public class TecnicoResources {
	
	@Autowired
	private TecnicoService service;
	
	@PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')") 
	@GetMapping(value = "/{id}")
    public ResponseEntity<TecnicoDTO> findById(@PathVariable Integer id) {
        Tecnico obj = service.findById(id);
        return ResponseEntity.ok().body(new TecnicoDTO(obj));
    }
	

	@PreAuthorize("hasRole('ADMIN')") 
	@GetMapping
	public ResponseEntity<List<TecnicoDTO>> findAll(){
		List<Tecnico> list = service.findAll();
		List<TecnicoDTO> listDTO = list.stream()
				.map(obj -> new TecnicoDTO(obj))
				.collect(Collectors.toList());
		return ResponseEntity.ok().body(listDTO);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')") 
	@PostMapping
	public ResponseEntity<TecnicoDTO> create (@Valid @RequestBody TecnicoDTO objDTO){
		Tecnico newObj = service.create(objDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(newObj.getId())
				.toUri();
		return ResponseEntity.created(uri).build();
	}
	

	@PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')") 
	@PutMapping(value="/{id}")
	public ResponseEntity<TecnicoDTO> update(@PathVariable Integer id, @Valid @RequestBody TecnicoDTO objDTO){
			Tecnico obj = service.update(id, objDTO);
			return ResponseEntity.ok().body(new TecnicoDTO(obj));
		
	}
	

	@PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')") 
	@DeleteMapping (value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}