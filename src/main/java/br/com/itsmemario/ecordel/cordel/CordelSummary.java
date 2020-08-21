package br.com.itsmemario.ecordel.cordel;

import br.com.itsmemario.ecordel.author.AuthorSummary;

import java.util.Set;


public interface CordelSummary {

	Long getId();
	AuthorSummary getAuthor();
	String getTitle();
	String getXilogravura();
	String getDescription();
	Set<String> getTags();

}
