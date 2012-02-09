package org.omich.lang.words;

public class SimplePair {
	
	private String original;
	private String translate;
	
	public SimplePair(String languages, String original, String translate){
		this.original = original;
		this.translate = translate;
	}
	public SimplePair(){
		original = "";
		translate = "";
	}
	
	public void setOriginal(String original){
		this.original = original;
	}
	
	public void setTranslate(String translate){
		this.translate = translate;
	}
	
	public String getOriginal(){
		return original;
	}
	
	public String getTranslate(){
		return translate;
	}
	
}
