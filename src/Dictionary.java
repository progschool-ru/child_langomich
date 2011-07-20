import javax.microedition.rms.*;
import java.io.*;
import java.util.*;

public class Dictionary
{
        public final int ORIGINAL = 1;
        public final int TRANSLATION = 2;
        public final int RATING = 3;
        public final int LANGUAGE = 4;
        public final int LAST_TIMING = 5;

        private RecordStore rs = null;
        private RecordEnumeration re;
        private Filter filter;

        private ByteArrayOutputStream byteOutputStream;
        private DataOutputStream writer;
        private ByteArrayInputStream byteInputStream;
        private DataInputStream reader;
        
        private String language;

        Dictionary()
        {
                dictionaryInit(null);
        }
        Dictionary(String language)
        {
                this.language = language;
                filter = new Filter(language);
                dictionaryInit(filter);
        }
        Dictionary(String language, long lastTiming)
        {
                this.language = language;
                filter = new Filter(language, lastTiming);
                dictionaryInit(filter);
        }
        private void dictionaryInit(Filter filter){
                byteOutputStream = new ByteArrayOutputStream();
                writer = new DataOutputStream(byteOutputStream);
        	try {
			rs = RecordStore.openRecordStore("Dicionary", true);
			re = rs.enumerateRecords(filter, new Ordering(ORIGINAL), false);
		}
		catch( RecordStoreException e ) {}
        }
	public String[] getColumn(int column)
	{
		if (getNumRecords() != 0)
	 	{
			String S[] = new String[getNumRecords()];
			for (int k = 0; k <  getNumRecords(); k++) {
				try {
					int id = re.nextRecordId();
					byte[] data = new byte[rs.getRecordSize(id)];
					data = rs.getRecord(id);
					byteInputStream = new ByteArrayInputStream(data);
					reader = new DataInputStream(byteInputStream);
                                        for(int j = 0; j < column; j++)
                                            S[k] = reader.readUTF();
				}
				catch(RecordStoreException e){}
				catch(IOException ioe){}
			}
			re.rebuild();
			return S;
		}
		else 
                    return null;
	}
	public String getCell(int row, int column)
	{
		if (getNumRecords() != 0)
	 	{
			String cell = new String();
			try {
				byte[] data = new byte[rs.getRecordSize(getId(row))];
				data = rs.getRecord(getId(row));
				byteInputStream = new ByteArrayInputStream(data);
				reader = new DataInputStream(byteInputStream);
                                for(int j = 0; j < column; j++)
                                        cell = reader.readUTF();
			}
			catch(RecordStoreException e){}
			catch(IOException ioe){}
			re.rebuild();
			return cell;
		}
		else 
                    return null;
	}
        public String[] getRecords(){
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
        public void addRecord(String original, String translation, int rating)
	{
            int id = getId(original);
            if(id != 0)
                setRecord(original, translation, rating, id);
            else
                newRecord(original, translation, rating);
        }
        public void addRecord(String original, String translation, int rating, long lastModified)
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
        public void newRecord(String original, String translation, int rating)
	{
		try{
                        Date date = new Date();
			writer.writeUTF(original);
			writer.writeUTF(translation);
			writer.writeUTF(Integer.toString(rating));
                        writer.writeUTF(language);
                        writer.writeUTF(Long.toString(date.getTime()));
			byte[] data = byteOutputStream.toByteArray();
			rs.addRecord( data, 0, data.length );
			writer.flush();
			byteOutputStream.reset();
		}
		catch( RecordStoreException e ){}
		catch(IOException ioe){}
		re.rebuild();
        }
        public void newRecord(String original, String translation, int rating, long lastModified)
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
		try{
                        Date date = new Date();
			writer.writeUTF(original);
			writer.writeUTF(translation);
			writer.writeUTF(Integer.toString(rating));
                        writer.writeUTF(language);
                        writer.writeUTF(Long.toString(date.getTime()));
			byte[] data = byteOutputStream.toByteArray();
			rs.setRecord(id, data, 0, data.length );
			writer.flush();
			byteOutputStream.reset();
		}
		catch( RecordStoreException e ){}
		catch(IOException ioe){}
		re.rebuild();
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
        public int getId(int row)
	{
		int id = 0;
		for(int i = 0;i < row; i++ ) {
			try {
				id = re.nextRecordId();
			}
			catch(RecordStoreException e){}
		}
		re.rebuild();
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
        public int getNumRecords() {
                return re.numRecords();
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
        public void destroy()
        {
		try
		{
			rs.closeRecordStore();
                        re.destroy();
		}
		catch (RecordStoreException e) { }
                try {
                        writer.close();
                        byteOutputStream.close();
                }
                catch(IOException ioe){}    
        }
}