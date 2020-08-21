package br.com.itsmemario.ecordel.author;

import br.com.itsmemario.ecordel.cordel.Cordel;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String about;
    private String email;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private Set<Cordel> cordels = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Set<Cordel> getCordels() {
        return cordels;
    }

    public void setCordels(Set<Cordel> cordels) {
        this.cordels = cordels;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
