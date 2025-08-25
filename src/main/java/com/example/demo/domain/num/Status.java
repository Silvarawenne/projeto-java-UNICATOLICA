package com.example.demo.domain.num;

public enum Status {

	ABERTO(0, "ABERTO"),
	ANDAMENTO(1, "ANDAMENTO"),
	FECHADO(2, "FECHADO");
	
	
	private Integer codigo;
	private String descricao;
	
	
	
	private Status( Integer codigo, String descricao) {
		this.codigo = codigo;
		this.descricao= descricao;
	}
	
	
	
	public Integer getCodigo(){
		return codigo;
	}
	
	
	
	public String descricao() {
		return descricao;
	}
	
	
	
	public static Status toEnum(Integer codigo) {
		if (codigo == null) {
			return null;
		}
		
		for(Status x : Status.values()) {
			if(codigo.equals(x.getCodigo())) {
				return x;
			}
		}
		
		throw new IllegalArgumentException("Status Inválido: " + codigo);
	}
	
	
	
	
	
	
	
	
}
