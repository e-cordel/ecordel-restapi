package br.com.itsmemario.ecordel.cordel;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Cordel {
	
	@Id
	private Long id;
	@NotBlank
	private String author;
	@NotBlank
	private String text;
	private String xilogravura;
	
	Cordel() {}

	public Cordel(String author, String text, String xilogravura) {
		super();
		this.author = author;
		this.text = text;
		this.xilogravura = xilogravura;
	}
	
	public Long getId() {
		return id;
	}

	public String getAuthor() {
		return author;
	}

	public String getText() {
		return text;
	}

	public String getXilogravura() {
		return xilogravura;
	}
	
}
