package org.omich.lang.words;

import java.util.List;

public class Language {
	
	private String name;
	private String server_id;
	private int id;
	
	private List<Word> words;
	
	public Language(String name, String server_id, int id, List<Word> words){
		
		this.name = name;
		this.server_id = server_id;
		this.id = id;
		this.words = words;
	}
	
	public Language(String name, String server_id, List<Word> words){
		this.name = name;
		this.server_id = server_id;
		this.words = words;
	}
	
	public Language(String name, int id, String server_id){
		this.name = name;
		this.id = id;
		this.server_id = server_id;
	}
	
	public Language(String name, int id){
		this.name = name;
		this.id = id;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public int getId(){
		return id;
	}
	public String getServerId(){
		return server_id;
	}
	
	public void setServerId(String id){
		this.server_id = id;
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
