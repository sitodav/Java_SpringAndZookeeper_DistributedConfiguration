package spring_zookeeper_puparuoli;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype") //perchè altrimenti schiatta cercando di caricare una proprietà quando parte la priam volta il contesto che non è stata
//ancora caricata
public class Studente {

	@Value("${task.task_program5}") //proprietà usando zookeeper
	private String nome;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
}
