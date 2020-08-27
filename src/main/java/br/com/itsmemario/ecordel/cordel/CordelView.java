package br.com.itsmemario.ecordel.cordel;

import br.com.itsmemario.ecordel.author.AuthorView;

import java.util.Set;


public interface CordelView {

	Long getId();
	AuthorView getAuthor();
	String getTitle();
	String getXilogravura();
	String getDescription();
	Set<String> getTags();

}
