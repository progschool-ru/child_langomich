import javax.microedition.rms.*;
import java.io.*;
import java.util.*;

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
		try{
			writer.writeUTF(original);
			writer.writeUTF(translation);
			writer.writeUTF(Integer.toString(rating));
                        writer.writeUTF(language);
                        writer.writeUTF(Long.toString(lastModified));
			byte[] data = byteOutputStream.toByteArray();
			rs.addRecord( data, 0, data.length );
			writer.flush();
			byteOutputStream.reset();
		}
		catch( RecordStoreException e ){}
		catch(IOException ioe){}
		re.rebuild();
        }
        private void setRecord(String original, String translation, int rating, int id)
	{
		Date date = new Date();
		setRecord(original, translation, rating, id, date.getTime());
        }
        private void setRecord(String original, String translation, int rating, int id, long lastModified)
	{
		try{
			writer.writeUTF(original);
			writer.writeUTF(translation);
			writer.writeUTF(Integer.toString(rating));
                        writer.writeUTF(language);
                        writer.writeUTF(Long.toString(lastModified));
			byte[] data = byteOutputStream.toByteArray();
			rs.setRecord(id, data, 0, data.length );
			writer.flush();
			byteOutputStream.reset();
		}
		catch( RecordStoreException e ){}
		catch(IOException ioe){}
		re.rebuild();
        }
        public void deleteRecord(int n)
	{
		int id = getId(n);
		try {
			rs.deleteRecord(id);
		}
		catch( RecordStoreException e ){}
		re.rebuild();
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
                    re.rebuild();
                    return true;
                }
                else
                {
                    if(rating < mi)
                        rating = 0;
                    else
                        rating -= mi;
                    setRecord(getCell(row, ORIGINAL), getCell(row, TRANSLATION), rating, id);
                    re.rebuild();
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
            long lastModified = 0;
            if (getNumRecords() != 0)
            {
                String record = "0";
                try {

                        byte[] data = new byte[rs.getRecordSize(id)];
                        data = rs.getRecord(id);
                        byteInputStream = new ByteArrayInputStream(data);
                        reader = new DataInputStream(byteInputStream);
                        for(int j = 0; j < LAST_TIMING; j++)
                            record = reader.readUTF();
                }
                catch(RecordStoreException e){}
                catch(IOException ioe){}
                lastModified = Long.parseLong(record);
            }
            return lastModified;
        }
        public void newOrdering(int column)
        {
            if(column != 1 && column != 2) {
                return;
            }
            try {
                re = rs.enumerateRecords(null, new Ordering(column), false);
            }
            catch( RecordStoreException e ) {} 
        }
}