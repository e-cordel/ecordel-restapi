package br.com.itsmemario.ecordel.cordel;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
public class Cordel {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	private String author;
	@NotBlank
	private String text;
	private String xilogravura;
	private String description;
	
	Cordel() {}

	public Cordel(String author, String text, 
			String xilogravura, String description) {
		super();
		this.author = author;
		this.text = text;
		this.xilogravura = xilogravura;
		this.description = description;
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

	public String getDescription() {
		return description;
	}
	
	
}
