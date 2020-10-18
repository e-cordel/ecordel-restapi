package br.com.itsmemario.ecordel.cordel;

import br.com.itsmemario.ecordel.author.Author;
import br.com.itsmemario.ecordel.xilogravura.Xilogravura;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;
import java.util.Set;

@Entity
public class Cordel implements CordelView{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "author_id")
	private Author author;
	@NotBlank
	private String title;
	@NotBlank
	private String content;
	@OneToOne
	@JoinColumn(name = "xilogravura_id")
	private Xilogravura xilogravura;
	private String description;
	@ElementCollection
	@CollectionTable(name = "cordel_tags")
	private Set<String> tags;

	Cordel() {}

	public static Cordel of(Long id) {
		Cordel cordel = new Cordel();
		cordel.id = id;
		return cordel;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Xilogravura getXilogravura() {
		return xilogravura;
	}

	public void setXilogravura(Xilogravura xilogravura) {
		this.xilogravura = xilogravura;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Cordel cordel = (Cordel) o;
		return Objects.equals(id, cordel.id) &&
				Objects.equals(title, cordel.title);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, title);
	}
}
