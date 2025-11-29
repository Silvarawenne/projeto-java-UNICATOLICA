package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
    
	// MÉTODO DE CONVERSÃO DTO -> ENTIDADE (Responsável pela criptografia)
	public Tecnico fromDTO(TecnicoDTO objDTO) {
        // Assume que a Entidade Tecnico tem um construtor que aceita o DTO
		Tecnico obj = new Tecnico(objDTO);
		
		// CRIPTOGRAFA A SENHA ANTES DE CONSTRUIR O OBJETO FINAL
		if (objDTO.getSenha() != null && !objDTO.getSenha().isEmpty()) {
            obj.setSenha(encoder.encode(objDTO.getSenha()));
        }
		
		return obj;
	}
	
	public Tecnico findById(Integer id) {
		
		Optional<Tecnico> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: "+id));
		
	}
	
	public List<Tecnico> findAll(){
		return repository.findAll();
	}
	
	/**
	 * Cria um novo técnico.
	 */
	public Tecnico create(TecnicoDTO objDTO){
		// 1. Garante que é uma nova inserção
		objDTO.setId(null);
		
		// 2. Valida duplicidade (lança exceção)
		validaPorCpfEEmail(objDTO);
		
		// 3. Converte DTO para entidade, criptografando a senha dentro do fromDTO
		Tecnico newObj = fromDTO(objDTO);
		
		// 4. Salva a entidade
		return repository.save(newObj);
	}
	
	/**
	 * Atualiza um técnico existente.
	 */
	public Tecnico update(Integer id, TecnicoDTO objDTO) {
		objDTO.setId(id);
		
		// 1. Busca o objeto antigo
		Tecnico oldObj = findById(id);
		
		// 2. Valida CPF/Email se houverem mudado
		if(!objDTO.getCpf().equals(oldObj.getCpf()) || !objDTO.getEmail().equals(oldObj.getEmail())) {
			validaPorCpfEEmail(objDTO);
		}

		// 3. Converte DTO para Entidade (fromDTO criptografa se houver nova senha)
		Tecnico newObj = fromDTO(objDTO);
		
		// 4. Preserva a senha antiga se nenhuma nova for fornecida
		if(objDTO.getSenha() == null || objDTO.getSenha().isEmpty()) {
			newObj.setSenha(oldObj.getSenha());
		}
		
		// Mantém o ID e a data de criação
		newObj.setId(oldObj.getId());
		newObj.setDataCriacao(oldObj.getDataCriacao()); 

		return repository.save(newObj);
	}
	
	public void delete(Integer id) {
        findById(id);
        repository.deleteById(id);
    }
	
	
	private void validaPorCpfEEmail(TecnicoDTO objDTO) {
        Optional<Pessoa> obj = pessoaRepository.findByCpf(objDTO.getCpf());
        if (obj.isPresent() && obj.get().getId() != objDTO.getId()) {
            // Se esta exceção for lançada, o Exception Handler retornará 400 Bad Request
            throw new DataIntegrityViolationException("CPF já cadastrado no sistema!");
        }

        obj = pessoaRepository.findByEmail(objDTO.getEmail());
        if (obj.isPresent() && obj.get().getId() != objDTO.getId()) {
            // Se esta exceção for lançada, o Exception Handler retornará 400 Bad Request
            throw new DataIntegrityViolationException("E-mail já cadastrado no sistema!");
        }
    }
}