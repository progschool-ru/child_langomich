package org.smdme;

import java.util.Date;
import java.util.Random;

public class Dictionary extends Records
{
	public static final int ORIGINAL = 1;
	public static final int TRANSLATION = 2;
	public static final int RATING = 3;
	public static final int LANGUAGE = 4;
	public static final int LAST_TIMING = 5;
	public static final int THE_LAST_COLUMN = LAST_TIMING;

	public static final String NAME = "Dicionary";

        private String language;

        Dictionary()
        {
                recordStoreInit(NAME, null, new Ordering(ORIGINAL));
        }
        Dictionary(String language)
        {
                this.language = language;
                recordStoreInit(NAME, new Filter(language, null), new Ordering(ORIGINAL));
        }
        Dictionary(String language, Boolean activeOnly)
        {
                this.language = language;
                recordStoreInit(NAME, new Filter(language, activeOnly), new Ordering(ORIGINAL));
        }
        Dictionary(String language, long lastTiming)
        {
                this.language = language;
                recordStoreInit(NAME, new Filter(language, lastTiming), new Ordering(ORIGINAL));
        }
	
	public String getCell(int row, int column)
	{
		return getRecord(getId(row), column);
	}
        public String[] getFullRecords(){
            int numRecords = getNumRecords();
            if(numRecords > 0)
            {
                String fullRecords[][] = getAllFullRecords();
                String records[] = new String[numRecords];
                for(int i = 0; i < numRecords; i++)
                {
                        if(isEmpty(fullRecords[i][TRANSLATION-1]))
                        {
                                records[i] = fullRecords[i][ORIGINAL-1] + " - deleted";
                        }
                        else
                        {
                                records[i] = fullRecords[i][ORIGINAL-1] +" "+fullRecords[i][TRANSLATION-1] +" "+ fullRecords[i][RATING-1];
                        }
                }
                return records;
            }
            else
                return null;
        }
        public Words getAllWords()
        {
            int numRecords = getNumRecords();
            String fullRecords[][] = getAllFullRecords();
            Words words = new Words(numRecords);
            for(int i=0; i < numRecords; i++)
            {
                words.setOriginal(i, fullRecords[i][ORIGINAL-1]);
                words.setTranslation(i, fullRecords[i][TRANSLATION-1]);
                words.setRating(i, Integer.parseInt(fullRecords[i][RATING-1]));
                words.setLastTiming(i, Long.parseLong(fullRecords[i][LAST_TIMING-1]));
            }
            return words;
        }
        public Words getRandomWords(int wordsNumber)
	{
            int numRecords = getNumRecords();
            if(numRecords == 0)
                return null;
            Random random = new Random ();
            String fullRecords[][] = getAllFullRecords();
            int AllPoint = 0; 
            int []Point = new int[numRecords];
            for(int i = 0; i < numRecords; i++){
                if(isEmpty(fullRecords[i][ORIGINAL-1]))
                {
                        Point[i] = 0;
                }
                else
                {
                        Point[i] = 10 - Integer.parseInt(fullRecords[i][RATING-1]);
                }
                AllPoint += Point[i];
            }
            if(AllPoint <= 0)
            {
                return null;
            }
            int rows[] = new int[wordsNumber];
            for(int j = 0; j < wordsNumber;j++)
            {
                int r = Math.abs(random.nextInt())%AllPoint;
                System.out.println(r);
                for (int i = 0; i < numRecords; i++)
                {
                    r = r - Point[i];
                    if(r <= 0 ) 
                    {
                        rows[j] = i+1;
                        break;
                    }
                }
                for(int k = 0; k < j; k++)
                    if(rows[j] == rows[k])
                        j--;
            }
            String[] randomWords = new String[wordsNumber];
            for(int j = 0; j < wordsNumber; j++)
                randomWords[j] = fullRecords[rows[j]-1][ORIGINAL-1];
            return new Words(wordsNumber, rows, randomWords);
        }
        public void newRecord(String original, String translation, int rating)
				//TODO: (3.low) Следует более тщательно продумать названия setRecord, addRecord, newRecord.
				// Возможно, следует сделать их более длинными, но более раскрывающими суть,
				// потому что совсем не очевидно, чем addRecord отличается от newRecord, причём именно в эту сторону.
	{
            int id = getId(original);
            if(id != 0)
                setRecord(original, translation, rating, id);
            else
                addRecord(original, translation, rating);
        }
        public void newRecord(String original, String translation, int rating, long lastModified)
	{
            int id = getId(original);
            if(id != 0) 
            {
                if(lastModified > getLastModified(id))
                {
                    if(isEmpty(translation))
                    {
                            deleteRecord(id);
                    }
                    else
                    {
                            setRecord(original, translation, rating, id, lastModified);
                    }
                }
            }
            else if(!isEmpty(translation))
            {
                addRecord(original, translation, rating, lastModified);
            }
        }
        public void addRecord(String original, String translation, int rating)
	{
		Date date = new Date();
		addRecord(original, translation, rating, date.getTime());
        }
        public void addRecord(String original, String translation, int rating, long lastModified)
	{
            String [] str = new String[5];
            str[0] = original;
            str[1] = translation;
            str[2] = Integer.toString(rating);
            str[3] = language;
            str[4] = Long.toString(lastModified);
            addRecord(str);
        }
        private void setRecord(String original, String translation, int rating, int id)
	{
		Date date = new Date();
		setRecord(original, translation, rating, id, date.getTime());
        }
        private void setRecord(String original, String translation, int rating, int id, long lastModified)
	{
            String [] str = new String[5];
            str[0] = original;
            str[1] = translation;
            str[2] = Integer.toString(rating);
            str[3] = language;
            str[4] = Long.toString(lastModified);
            setRecord(str, id);
        }
	public void deleteTranslation(int row)
	{
		int id = getId(row);
		String[] record = getFullRecord(id, THE_LAST_COLUMN);
		record[TRANSLATION - 1] = "";
		record[LAST_TIMING - 1] = Long.toString(System.currentTimeMillis());
		setRecord(record, id);
	}
        public Words answer(Words words)
	{
                int pl = 2;
                int mi = 3;
                int rating;
                int row;
                String fullRecords[][] = getAllFullRecords();
                for(int i = 0; i < words.getWordsNumber();i++)
                {
                    row = words.getRow(i);
                    rating = Integer.parseInt(fullRecords[row-1][RATING-1]);                
                    if(fullRecords[row-1][TRANSLATION-1].equals(words.getTranslation(i)))
                    {
                        if(rating  > 9-pl)
                            rating  = 9;
                        else
                            rating += pl;
                        words.setAnswer(i, true);
                    }
                    else
                    {
                        if(rating < mi)
                            rating = 0;
                        else
                            rating -= mi;
                        words.setAnswer(i, false);
                    }
                    words.setTranslation(i, fullRecords[row-1][TRANSLATION-1]);
                    words.setRating(i, rating);
                    setRecord(fullRecords[row-1][ORIGINAL-1], fullRecords[row-1][TRANSLATION-1], rating, getId(row));
                }
                return words;
        }
        public int getId(String original)
	{
		String [] originals = getColumn(ORIGINAL);
		if(originals == null)
		{
			return 0;
		}
		int id = 0;
		for(int i = 0;i < originals.length; i++ ) {
                    if(originals[i].equals(original)){
                        id = getId(i+1);
                        break;
                    }
		}
		return id;
        }
        private long getLastModified(int id)
        {
            String record = getRecord(id, LAST_TIMING);
            if(record!=null)
                return Long.parseLong(record);
            else
                return 0;
        }
		
	private boolean isEmpty(String string)
	{
		return "".equals(string);
	}
}