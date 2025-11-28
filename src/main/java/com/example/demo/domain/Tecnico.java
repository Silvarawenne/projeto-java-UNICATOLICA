package com.example.demo.domain; // Pacote correto!

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
import javax.persistence.Table; // <-- NOVO IMPORT
import javax.persistence.PrimaryKeyJoinColumn; // <-- NOVO IMPORT

import com.example.demo.domain.dtos.TecnicoDTO;
import com.example.demo.domain.num.Perfil;

@Entity
@Table(name = "TECNICO") // <-- ADICIONE ISTO
@PrimaryKeyJoinColumn(name = "id") // <-- ADICIONE ISTO
public class Tecnico extends Pessoa {
    
    private static final long serialVersionUID = 1L;

    @OneToMany(mappedBy = "tecnico")
    private List<Chamado> chamados = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "PERFIS_TECNICO")
    private Set<Integer> perfis = new HashSet<>();

    public Tecnico() {
        super();
        addPerfil(Perfil.TECNICO);
    }

    public Tecnico(Integer id, String nome, String cpf, String email, String senha) {
        super(id, nome, cpf, email, senha);
        addPerfil(Perfil.TECNICO);
    }

    public Tecnico(TecnicoDTO obj) {
        super();
        this.id = obj.getId();
        this.nome = obj.getNome();
        this.cpf = obj.getCpf();
        this.email = obj.getEmail();
        this.senha = obj.getSenha();
        addPerfil(Perfil.TECNICO);
    }

	public void addPerfil(Perfil perfil) {
        this.perfis.add(perfil.getCodigo());
    }
    
    public Set<Perfil> getPerfis() {
		return perfis.stream().map(x -> Perfil.toEnum(x)).collect(Collectors.toSet());
	}

    public List<Chamado> getChamados() {
        return chamados;
    }

    public void setChamados(List<Chamado> chamados) {
        this.chamados = chamados;
    }
}