package util;

import java.util.Map;
import java.io.Serializable;
import java.util.HashMap;

public class Arquivo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String operacao;
	private Status status;
	
	Map<String, Object>param;
	
	public Arquivo(String operacao){
		this.operacao = operacao;
		param = new HashMap<>();
	}
	
	public Status getStatus() {
		return status;
	}
	
	public void setStatus(Status s) {
		this.status = s;
	}

	public void setParam(String chave, Object valor) {
		param.put(chave,  valor);
	}
	
	public Object getParam(String chave) {
		return param.get(chave);
	}
	public String getOperação() {
		return operacao;
	}
	
	@Override
	public String toString() {
		String m = "Operação: "+operacao;
		m += "\nStatus: "+status;
		
		m += "\nParâmetros:";
		for(String p : param.keySet()) {
			m += "\n"+p+": "+param.get(p);
		}
		
		return m;
	}
}
