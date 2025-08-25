package com.example.demo.domai;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.example.demo.domain.num.Perfil;

@Entity
public class Tecnico extends Pessoa {
    
    private static final long serialVersionUID = 1L;

    @OneToMany(mappedBy = "tecnico")
    private List<Chamado> chamados = new ArrayList<>();

    // Adicionamos a coleção de perfis AQUI, com sua própria tabela
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "PERFIS_TECNICO")
    private Set<Integer> perfis = new HashSet<>();

    public Tecnico() {
        super();
        addPerfil(Perfil.TECNICO); // Define o perfil padrão
    }

    public Tecnico(Integer id, String nome, String cpf, String email, String senha) {
        super(id, nome, cpf, email, senha);
        addPerfil(Perfil.TECNICO);
    }

    public void addPerfil(Perfil perfil) {
        this.perfis.add(perfil.getCodigo());
    }

    public List<Chamado> getChamados() {
        return chamados;
    }

    public void setChamados(List<Chamado> chamados) {
        this.chamados = chamados;
    }

    public Set<Perfil> getPerfis() {
		return perfis.stream().map(x -> Perfil.toEnum(x)).collect(Collectors.toSet());
	}
}