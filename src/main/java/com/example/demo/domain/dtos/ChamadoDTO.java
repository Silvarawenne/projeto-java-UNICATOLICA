package com.example.demo.domain.dtos;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;

import com.example.demo.domain.Chamado;
import com.fasterxml.jackson.annotation.JsonFormat;

public class ChamadoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private LocalDateTime dataAbertura;

	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private LocalDateTime dataFechamento;

	@NotNull(message = "O campo PRIORIDADE é requerido")
	private Integer prioridade;
	private String nomePrioridade;

	@NotNull(message = "O campo STATUS é requerido")
	private Integer status;
	private String nomeStatus;

	@NotNull(message = "O campo TÍTULO é requerido")
	private String titulo;

	@NotNull(message = "O campo OBSERVAÇÕES é requerido")
	private String observacoes;

	// O campo TÉCNICO pode ser nulo em Chamado se a atribuição for opcional.
	// A anotação @NotNull aqui significa que ao CRIAR ou ATUALIZAR, o ID do técnico é requerido no DTO.
	// Para chamados já existentes que podem ter técnico nulo no banco, a lógica do construtor gerencia isso.
	@NotNull(message = "O campo TÉCNICO é requerido")
	private Integer tecnicoId;
	private String nomeTecnico;

	// O campo CLIENTE é sempre requerido para um chamado.
	@NotNull(message = "O campo CLIENTE é requerido")
	private Integer clienteId;
	private String nomeCliente;

	/**
	 * Construtor padrão sem argumentos.
	 * Necessário para serialização/desserialização JSON.
	 */
	public ChamadoDTO() {
		super();
	}

	/**
	 * Construtor que recebe um objeto Chamado de domínio e popula o DTO.
	 * Realiza a conversão da entidade para o DTO, tratando campos potencialmente nulos.
	 *
	 * @param obj O objeto Chamado de domínio a ser convertido.
	 */
	public ChamadoDTO(Chamado obj) {
		this.id = obj.getId();
		this.dataAbertura = obj.getDataAbertura();
		this.dataFechamento = obj.getDataFechamento();
		this.prioridade = obj.getPrioridade().getCodigo();
		this.nomePrioridade = obj.getPrioridade().getDescricao();
		this.status = obj.getStatus().getCodigo();
		this.nomeStatus = obj.getStatus().getDescricao();
		this.titulo = obj.getTitulo();
		this.observacoes = obj.getObservacoes();
		
		// Correção para NullPointerException:
		// Verifica se o objeto Tecnico associado ao Chamado não é nulo antes de acessar seus atributos.
		if (obj.getTecnico() != null) {
			this.tecnicoId = obj.getTecnico().getId();
			this.nomeTecnico = obj.getTecnico().getNome();
		} else {
			this.tecnicoId = null; // Se não houver técnico, define como nulo no DTO
			this.nomeTecnico = null; // Se não houver técnico, define como nulo no DTO
		}
		
		// O cliente é esperado ser sempre não nulo em um Chamado válido
		this.clienteId = obj.getCliente().getId();
		this.nomeCliente = obj.getCliente().getNome();
	}

	// --- Getters e Setters com Javadoc ---

	/**
	 * Retorna o ID único do chamado.
	 * @return O ID do chamado.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Define o ID único do chamado.
	 * @param id O ID do chamado.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Retorna a data e hora de abertura do chamado.
	 * @return A data e hora de abertura.
	 */
	public LocalDateTime getDataAbertura() {
		return dataAbertura;
	}

	/**
	 * Define a data e hora de abertura do chamado.
	 * @param dataAbertura A data e hora de abertura.
	 */
	public void setDataAbertura(LocalDateTime dataAbertura) {
		this.dataAbertura = dataAbertura;
	}

	/**
	 * Retorna a data e hora de fechamento do chamado.
	 * @return A data e hora de fechamento.
	 */
	public LocalDateTime getDataFechamento() {
		return dataFechamento;
	}

	/**
	 * Define a data e hora de fechamento do chamado.
	 * @param dataFechamento A data e hora de fechamento.
	 */
	public void setDataFechamento(LocalDateTime dataFechamento) {
		this.dataFechamento = dataFechamento;
	}

	/**
	 * Retorna o código numérico da prioridade do chamado.
	 * @return O código da prioridade.
	 */
	public Integer getPrioridade() {
		return prioridade;
	}

	/**
	 * Define o código numérico da prioridade do chamado.
	 * @param prioridade O código da prioridade.
	 */
	public void setPrioridade(Integer prioridade) {
		this.prioridade = prioridade;
	}

	/**
	 * Retorna a descrição da prioridade do chamado.
	 * @return A descrição da prioridade.
	 */
	public String getNomePrioridade() {
		return nomePrioridade;
	}

	/**
	 * Define a descrição da prioridade do chamado.
	 * @param nomePrioridade A descrição da prioridade.
	 */
	public void setNomePrioridade(String nomePrioridade) {
		this.nomePrioridade = nomePrioridade;
	}

	/**
	 * Retorna o código numérico do status do chamado.
	 * @return O código do status.
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * Define o código numérico do status do chamado.
	 * @param status O código do status.
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * Retorna a descrição do status do chamado.
	 * @return A descrição do status.
	 */
	public String getNomeStatus() {
		return nomeStatus;
	}

	/**
	 * Define a descrição do status do chamado.
	 * @param nomeStatus A descrição do status.
	 */
	public void setNomeStatus(String nomeStatus) {
		this.nomeStatus = nomeStatus;
	}

	/**
	 * Retorna o título do chamado.
	 * @return O título do chamado.
	 */
	public String getTitulo() {
		return titulo;
	}

	/**
	 * Define o título do chamado.
	 * @param titulo O título do chamado.
	 */
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	/**
	 * Retorna as observações do chamado.
	 * @return As observações do chamado.
	 */
	public String getObservacoes() {
		return observacoes;
	}

	/**
	 * Define as observações do chamado.
	 * @param observacoes As observações do chamado.
	 */
	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	/**
	 * Retorna o ID do técnico associado ao chamado. Pode ser nulo se não houver técnico atribuído.
	 * @return O ID do técnico.
	 */
	public Integer getTecnicoId() {
		return tecnicoId;
	}

	/**
	 * Define o ID do técnico associado ao chamado.
	 * @param tecnicoId O ID do técnico.
	 */
	public void setTecnicoId(Integer tecnicoId) {
		this.tecnicoId = tecnicoId;
	}

	/**
	 * Retorna o nome do técnico associado ao chamado. Pode ser nulo se não houver técnico atribuído.
	 * @return O nome do técnico.
	 */
	public String getNomeTecnico() {
		return nomeTecnico;
	}

	/**
	 * Define o nome do técnico associado ao chamado.
	 * @param nomeTecnico O nome do técnico.
	 */
	public void setNomeTecnico(String nomeTecnico) {
		this.nomeTecnico = nomeTecnico;
	}

	/**
	 * Retorna o ID do cliente que abriu o chamado.
	 * @return O ID do cliente.
	 */
	public Integer getClienteId() {
		return clienteId;
	}

	/**
	 * Define o ID do cliente que abriu o chamado.
	 * @param clienteId O ID do cliente.
	 */
	public void setClienteId(Integer clienteId) {
		this.clienteId = clienteId;
	}

	/**
	 * Retorna o nome do cliente que abriu o chamado.
	 * @return O nome do cliente.
	 */
	public String getNomeCliente() {
		return nomeCliente;
	}

	/**
	 * Define o nome do cliente que abriu o chamado.
	 * @param nomeCliente O nome do cliente.
	 */
	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}
}