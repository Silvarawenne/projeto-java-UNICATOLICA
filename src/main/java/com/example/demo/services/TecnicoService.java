package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repositories.PessoaRepository;
import com.example.demo.services.exceptions.DataIntegrityViolationException;
import com.example.demo.repositories.TecnicoRepository;
import com.example.demo.domain.Pessoa;
import com.example.demo.domain.Tecnico;
import com.example.demo.domain.dtos.TecnicoDTO;
import com.example.demo.services.exceptions.ObjectNotFoundException;

@Service
public class TecnicoService {
	
	
	@Autowired
	private TecnicoRepository repository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	public Tecnico findById(Integer id) {
		
		Optional<Tecnico> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: "+id));
		
	}
	
	public List<Tecnico> findAll(){
		return repository.findAll();
	}
	
	public Tecnico create(TecnicoDTO objDTO){
		objDTO.setId(null);
		validaPorCpfEEmail(objDTO);
		Tecnico newObj = new Tecnico(objDTO);
		return repository.save(newObj);
	}
	
	public Tecnico update(Integer id, TecnicoDTO objDTO) {
        objDTO.setId(id); 
        Tecnico oldObj = findById(id); 
        validaPorCpfEEmail(objDTO);
        updateData(oldObj, objDTO);
        return repository.save(oldObj);
    }

   
    private void updateData(Tecnico oldObj, TecnicoDTO objDTO) {
        oldObj.setNome(objDTO.getNome());
        oldObj.setCpf(objDTO.getCpf());
        oldObj.setEmail(objDTO.getEmail());
        oldObj.setSenha(objDTO.getSenha());
    }
    
    
    public void delete(Integer id) {
    	Tecnico obj = findById(id); 	
    	repository.deleteById(id);
    }
    
    private void validaPorCpfEEmail(TecnicoDTO objDTO) {
        Optional<Pessoa> obj = pessoaRepository.findByCpf(objDTO.getCpf());
        if (obj.isPresent() && obj.get().getId() != objDTO.getId()) {
            throw new DataIntegrityViolationException("CPF já cadastrado no sistema!");
        }

        obj = pessoaRepository.findByEmail(objDTO.getEmail());
        if (obj.isPresent() && obj.get().getId() != objDTO.getId()) {
            throw new DataIntegrityViolationException("E-mail já cadastrado no sistema!");
        }
    }
        
        

}
