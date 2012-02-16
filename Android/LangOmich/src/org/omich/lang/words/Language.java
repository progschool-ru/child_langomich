package org.omich.lang.words;

import java.util.List;

public class Language {
	
	private String name;
	private String id;
	
	private List<Word> words;
	
	public Language(String name, String id, List<Word> words){
		
		this.name = name;
		this.id = id;
		this.words = words;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getId(){
		return id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public List<Word> getWords(){
		return words;
	}
	
	public void setWords(List<Word> words ){
		this.words = words;
	}
	
	@Override
	public String toString(){
		return name;
	}

}
