package org.omich.lang.words;

public class Word {
	
	private String original;
	private String translation;
	private int rating;
	private long modified;
	
	public Word(String original, String translation, int rating){
		
		this.rating = rating;
		this.translation = translation;
		this.rating = rating;
		this.modified = 0;
	}
	
	public Word(String original, String translation, int rating, long modified){
		
		this.rating = rating;
		this.original = original;
		this.translation = translation;
		this.modified = modified;
	}
	
	public String getOriginal(){
		return original;
	}
	
	public void setOriginal(String original){
		this.original = original;
	}
	
	public String getTranslation(){
		return translation;
	}
	
	public void setTranslation(String translation){
		this.translation = translation;
	}
	
	public int getRating(){
		return rating;
	}
	
	public void setRating(int rating){
		this.rating = rating;
	}
	
	public long getModified(){
		return modified;
	}
	
	public void setModified(long modified){
		this.modified = modified;
	}
	
	@Override
	public String toString(){
		return original+" "+translation;
	}
}
