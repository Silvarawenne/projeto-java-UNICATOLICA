package com.example.demo.services;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Chamado;
import com.example.demo.domain.Cliente;
import com.example.demo.domain.Tecnico;
import com.example.demo.domain.dtos.ChamadoDTO;
import com.example.demo.domain.num.Prioridade;
import com.example.demo.domain.num.Status;
import com.example.demo.repositories.ChamadoRepository;
import com.example.demo.services.exceptions.ObjectNotFoundException;


@Service
public class ChamadoService {
	
	@Autowired
	private ChamadoRepository repository;
	@Autowired
	private TecnicoService tecnicoService;
	@Autowired
	private ClienteService clienteService;
	
	public Chamado findById(Integer id) {
		Optional<Chamado> obj = repository.findById(id);
		return obj.orElseThrow(()-> new ObjectNotFoundException("Objeto não encontrado! ID: " +id));
	}
	
	public List<Chamado> findAll(){
		return repository.findAll();
	}
	
	
	public Chamado create (ChamadoDTO objDTO) {
		return repository.save(newChamado(objDTO));
	}
	
	public Chamado update(Integer id, ChamadoDTO objDTO) {
		objDTO.setId(id);
		Chamado oldObj = findById(id);
		Chamado newObj = newChamado(objDTO);
		newObj.setId(oldObj.getId());
		newObj.setDataAbertura(oldObj.getDataAbertura());
		return repository.save(newObj);
	}
	
	
	private Chamado newChamado(ChamadoDTO objDTO) {
		Tecnico tecnico = tecnicoService.findById(objDTO.getTecnicoId());
		Cliente cliente = clienteService.findById(objDTO.getClienteId());
		
		Chamado chamado = new Chamado();
		if(objDTO.getId() != null) {
			chamado.setId(objDTO.getId());
		}
		
		if(objDTO.getStatus().equals(2)) {
			chamado.setDataFechamento(LocalDateTime.now());
		}
		
		chamado.setTecnico(tecnico);
		chamado.setCliente(cliente);
		chamado.setPrioridade(Prioridade.toEnum(objDTO.getPrioridade()));
		chamado.setStatus(Status.toEnum(objDTO.getStatus()));
		chamado.setTitulo(objDTO.getTitulo());
		chamado.setObservacoes(objDTO.getObservacoes());
		
		return chamado;
	}
	
	
	public void delete(Integer id) {
    	Chamado obj = findById(id); 	
    	repository.deleteById(id);
    }
	
	

}
