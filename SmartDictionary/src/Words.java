public class Words 
{
    private int wordsNumber;
    
    private int rows[];
    private String original[];
    private String translation[];
    private int rating[];   
    private boolean answer[];
    
    Words(int wordsNumber, int rows[], String original[])
    {
           this.wordsNumber = wordsNumber;
           this.rows = rows;
           this.original = original;
           translation = new String[wordsNumber];
           rating = new int[wordsNumber]; 
           answer = new boolean[wordsNumber];
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
    public void setAnswer(int number, boolean answer) 
    {
       this.answer[number] = answer; 
    }
}
