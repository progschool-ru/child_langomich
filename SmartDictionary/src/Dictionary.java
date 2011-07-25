import java.util.Date;
import java.util.Random;

public class Dictionary extends Records
{
        public final int ORIGINAL = 1;
        public final int TRANSLATION = 2;
        public final int RATING = 3;
        public final int LANGUAGE = 4;
        public final int LAST_TIMING = 5;

        private String language;

        Dictionary()
        {
                recordStoreInit("Dicionary", null, new Ordering(ORIGINAL));
        }
        Dictionary(String language)
        {
                this.language = language;
                recordStoreInit("Dicionary", new Filter(language), new Ordering(ORIGINAL));
        }
        Dictionary(String language, long lastTiming)
        {
                this.language = language;
                recordStoreInit("Dicionary", new Filter(language, lastTiming), new Ordering(ORIGINAL));
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
                    Records[i] = Original[i] +" "+ Translation[i] +" "+ Rating[i];
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
            int []Point = new int[getNumRecords()];
            for(int i = 0; i < getNumRecords(); i++){
                Point[i] = 10 - Integer.parseInt(rating[i]);
                AllPoint += Point[i];
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
                    setRecord(original, translation, rating, id, lastModified);
                }
            }
            else
                newRecord(original, translation, rating, lastModified);
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
        public void deleteRecord(int row)
	{
		super.deleteRecord(getId(row));
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
		int id = 0;
                if(getColumn(ORIGINAL) == null)     
                    return id;
                String [] originals = getColumn(ORIGINAL);
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
}