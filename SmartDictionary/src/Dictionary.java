import java.util.Date;
import java.util.Random;
import java.util.Stack;

public class Dictionary extends Records
{
        public final int ORIGINAL = 1;
        public final int TRANSLATION = 2;
        public final int RATING = 3;
        public final int LANGUAGE = 4;
        public final int LAST_TIMING = 5;
		public final int THE_LAST_COLUMN = LAST_TIMING;

        public final String NAME = "Dicionary";

        private String language;

        Dictionary()
        {
                recordStoreInit(NAME, null, new Ordering(ORIGINAL));
        }
        Dictionary(String language)
        {
                this.language = language;
                recordStoreInit(NAME, new Filter(language), new Ordering(ORIGINAL));
        }
        Dictionary(String language, long lastTiming)
        {
                this.language = language;
                recordStoreInit(NAME, new Filter(language, lastTiming), new Ordering(ORIGINAL));
        }
		
	public void deleteEmptyWords()
	{
		Stack stack = new Stack();
		int number = getNumRecords();
		for(int i = 0; i < number; i++)
		{
			int id = getId(i + 1);
			String translation = getRecord(id, TRANSLATION);
			if(isEmpty(translation))
			{
				stack.push(new Integer(id));
			}
		}
		
		while(!stack.empty())
		{
			Integer id = (Integer)stack.pop();
			deleteRecord(id.intValue());
		}
	}
	
	public String getCell(int row, int column)
	{
		return getRecord(getId(row), column);
	}
        public String[] getFullRecords(){
            if(getNumRecords() > 0)
            {
                String []Records = new String[getNumRecords()];
                String []Original = getColumn(ORIGINAL);
                String []Translation = getColumn(TRANSLATION);
                String []Rating = getColumn(RATING);
                for(int i = 0; i < getNumRecords(); i++)
				{
					if(isEmpty(Translation[i]))
					{
						Records[i] = Original[i] + " - deleted";
					}
					else
					{
						Records[i] = Original[i] +" "+ Translation[i] +" "+ Rating[i];
					}
				}
                return Records;
            }
            else
                return null;
        }
        public int getRandomRow()
	{
            if(getNumRecords() == 0)
                return 0;
            Random random = new Random ();
            int AllPoint = 0;
            String []rating = getColumn(RATING);
			String []translation = getColumn(TRANSLATION);
            int []Point = new int[getNumRecords()];
            for(int i = 0; i < getNumRecords(); i++){
				if(isEmpty(translation[i]))
				{
					Point[i] = 0;
				}
				else
				{
					Point[i] = 10 - Integer.parseInt(rating[i]);
				}
                AllPoint += Point[i];
            }
			if(AllPoint <= 0)
			{
				return 0;
			}
            int r = Math.abs(random.nextInt())%AllPoint;
            for (int i = 0; i < getNumRecords(); i++)
		{
                    r = r - Point[i];
                    if(r <= 0 )
                        return i+1;
		}
            return getNumRecords();
        }
        public void newRecord(String original, String translation, int rating)
				//TODO: (3.low) Следует более тчательно продумать названия setRecord, addRecord, newRecord.
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
        public boolean answer(int row, String answer)
	{
		int id = getId(row);
                int rating = Integer.parseInt(getCell(row, RATING));
                int pl = 2;
                int mi = 3;
                if(getCell(row, TRANSLATION).equals(answer))
                {
                    if(rating  > 9-pl)
                        rating  = 9;
                    else
                        rating += pl;
                    setRecord(getCell(row, ORIGINAL), getCell(row, TRANSLATION), rating, id);
                    return true;
                }
                else
                {
                    if(rating < mi)
                        rating = 0;
                    else
                        rating -= mi;
                    setRecord(getCell(row, ORIGINAL), getCell(row, TRANSLATION), rating, id);
                    return false;
                }
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