package br.com.itsmemario.ecordel.cordel;

import br.com.itsmemario.ecordel.author.AuthorView;
import br.com.itsmemario.ecordel.xilogravura.Xilogravura;

import java.util.Set;


public interface CordelView {

	Long getId();
	AuthorView getAuthor();
	String getTitle();
	Xilogravura getXilogravura();
	String getDescription();
	Set<String> getTags();

}
