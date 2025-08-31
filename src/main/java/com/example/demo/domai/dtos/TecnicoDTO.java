package com.example.demo.domai.dtos;

import java.io.Serializable;
import java.time.LocalDateTime; // IMPORT CORRIGIDO
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.demo.domai.Tecnico;
import com.example.demo.domain.num.Perfil;
import com.fasterxml.jackson.annotation.JsonFormat;

public class TecnicoDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	protected Integer id;
	protected String nome;
	protected String cpf;
	protected String email;
	protected Set<Integer> perfis = new HashSet<>();
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	protected LocalDateTime dataCriacao; // TIPO CORRIGIDO
	
	public TecnicoDTO() {
		super();
	}
	
	public TecnicoDTO(Tecnico obj) {
		super();
		this.id = obj.getId();
		this.nome = obj.getNome();
		this.cpf = obj.getCpf();
		this.email = obj.getEmail();
		this.perfis = obj.getPerfis().stream().map(x -> x.getCodigo()).collect(Collectors.toSet());
		this.dataCriacao = obj.getDataCriacao();
	}
	
	// GETTERS E SETTERS (CORRIGIDOS)
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public Set<Perfil> getPerfis() {
        return perfis.stream().map(x -> Perfil.toEnum(x)).collect(Collectors.toSet());
    }
    
    public void addPerfil(Perfil perfil) {
    	this.perfis.add(perfil.getCodigo());
    }
}