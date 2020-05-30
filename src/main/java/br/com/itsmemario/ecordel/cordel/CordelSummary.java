package br.com.itsmemario.ecordel.cordel;

import java.util.Set;


interface CordelSummary {

	Long getId();
	String getAuthor();
	String getTitle();
	String getXilogravura();
	String getDescription();
	Set<String> getTags();

}
