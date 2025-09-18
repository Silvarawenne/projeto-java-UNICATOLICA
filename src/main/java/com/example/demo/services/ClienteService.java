package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Cliente;
import com.example.demo.domain.Pessoa;
import com.example.demo.domain.dtos.ClienteDTO;
import com.example.demo.repositories.ClienteRepository;
import com.example.demo.repositories.PessoaRepository;
import com.example.demo.services.exceptions.DataIntegrityViolationException;
import com.example.demo.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;
    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
    private BCryptPasswordEncoder pe;

    public Cliente findById(Integer id) {
        Optional<Cliente> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id));
    }

    public List<Cliente> findAll() {
        return repository.findAll();
    }

    public Cliente create(ClienteDTO objDTO) {
    	objDTO.setId(null);
    	validaPorCpfEEmail(objDTO);
    	Cliente newCliente = new Cliente(objDTO);
    	newCliente.setSenha(pe.encode(objDTO.getSenha()));
    	return repository.save(newCliente);
    }

    public Cliente update(Integer id, ClienteDTO objDTO) {
        objDTO.setId(id);
        Cliente oldObj = findById(id);
        validaPorCpfEEmail(objDTO);
        oldObj.setNome(objDTO.getNome());
        oldObj.setCpf(objDTO.getCpf());
        oldObj.setEmail(objDTO.getEmail());
        
        if(objDTO.getSenha() != null && !objDTO.getSenha().isEmpty()) {
        	oldObj.setSenha(pe.encode(objDTO.getSenha()));
        }
        return repository.save(oldObj);
    }

    public void delete(Integer id) {
        findById(id);
        repository.deleteById(id);
    }
    

    private void validaPorCpfEEmail(ClienteDTO objDTO) {
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