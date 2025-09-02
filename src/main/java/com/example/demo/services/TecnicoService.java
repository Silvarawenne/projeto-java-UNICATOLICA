package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domai.Tecnico;
import com.example.demo.repositories.TecnicoRepository;
import com.example.demo.domai.dtos.TecnicoDTO;
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
	
	public Tecnico create(TecnicoDTO objDTO){
		objDTO.setId(null);
		Tecnico newObj = new Tecnico(objDTO);
		return repository.save(newObj);
	}
	
	public Tecnico update(Integer id, TecnicoDTO objDTO) {
        objDTO.setId(id); // Garante que o ID no DTO é o mesmo da URL
        Tecnico oldObj = findById(id); // Busca o técnico existente no banco
        
        updateData(oldObj, objDTO); 
        
        // Salva as alterações no objeto existente
        return repository.save(oldObj);
    }

   
    private void updateData(Tecnico oldObj, TecnicoDTO objDTO) {
        oldObj.setNome(objDTO.getNome());
        oldObj.setCpf(objDTO.getCpf());
        oldObj.setEmail(objDTO.getEmail());
        oldObj.setSenha(objDTO.getSenha());
        
    }

}
