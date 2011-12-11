package org.smdme;

public class Words 
{
    private int wordsNumber;
    
    private int rows[];
    private String original[];
    private String translation[];
    private int rating[];   
    private boolean answer[];
    private long lastTiming[];
    
    Words(int wordsNumber, int rows[], String original[])
    {
           this.wordsNumber = wordsNumber;
           this.rows = rows;
           this.original = original;
           translation = new String[wordsNumber];
           rating = new int[wordsNumber]; 
           answer = new boolean[wordsNumber];
           lastTiming = new long[wordsNumber];
    }
    Words(int wordsNumber)
    {
           this.wordsNumber = wordsNumber;         
           original = new String[wordsNumber];
           translation = new String[wordsNumber];
           rating = new int[wordsNumber];
           answer = new boolean[wordsNumber];
           lastTiming = new long[wordsNumber];
           rows = new int[wordsNumber];
    }
    public int getWordsNumber() 
    {
       return wordsNumber; 
    }
    public int getRow(int number) 
    {
       return rows[number]; 
    }
    public String getOriginal(int number) 
    {
       return original[number]; 
    }
    public String getTranslation(int number) 
    {
       return translation[number]; 
    }
    public long getLastTiming(int number) 
    {
       return lastTiming[number]; 
    }
    public void setTranslation(int number, String translation)
    {
       this.translation[number] = translation;
    }    
    public int getRating(int number) 
    {
       return rating[number]; 
    }
    public boolean getAnswer(int number) 
    {
       return answer[number]; 
    }
    public void setRating(int number, int rating) 
    {
       this.rating[number] = rating; 
    }
    public void setOriginal(int number, String original) 
    {
       this.original[number] = original; 
    }
    public void setAnswer(int number, boolean answer) 
    {
       this.answer[number] = answer; 
    }
    
    public void setLastTiming(int number,long lastTiming) 
    {
       this.lastTiming[number] = lastTiming; 
    }
}
