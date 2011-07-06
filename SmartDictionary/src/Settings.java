import javax.microedition.rms.*;
import java.io.*;
import java.util.*;

public class Settings
{
        public final int NUMBER_OF_WORDS = 1;
        public final int LANGUAGE = 2;
        public final int LAST_TIMING = 3;
        public final int NUMBER_OF_TIMING = 4;
        public final int LOGIN = 5;
        public final int PASSWORD = 6;
        public final int URL = 7;
        public final int TEXT = 8;

        private RecordStore rs = null;
        private RecordEnumeration re;

        private ByteArrayOutputStream byteOutputStream;
        private DataOutputStream writer;
        private ByteArrayInputStream byteInputStream;
        private DataInputStream reader;

        Settings()
        {
                byteOutputStream = new ByteArrayOutputStream();
                writer = new DataOutputStream(byteOutputStream);
        	try {
			rs = RecordStore.openRecordStore("Settings", true);
			re = rs.enumerateRecords(null, null, false);
		}
		catch( RecordStoreException e ){}
        	if(getRecord(NUMBER_OF_WORDS) == null ||
                   getRecord(LANGUAGE) == null ||
                   getRecord(LAST_TIMING) == null ||
                   getRecord(NUMBER_OF_TIMING) == null)
                {
                    addRecord("1");
                    addRecord("null");
                    addRecord(Long.toString(new Date().getTime()));
                    addRecord("0");
                }
        	if(getRecord(LOGIN) == null ||
                   getRecord(PASSWORD) == null ||
                   getRecord(URL) == null)
                {
                    addRecord("null");
                    addRecord("null");
                    addRecord("localhost:8080");
                }
        	if(getRecord(TEXT) == null)
                {
                    addRecord("null");
                }
        }
        public void setNumberOfWords(int numberOfWords)
        {
            setRecord(Integer.toString(numberOfWords), NUMBER_OF_WORDS);
        }
        public int getNumberOfWords()
        {
            String record = getRecord(NUMBER_OF_WORDS);
            if(record!= null)
                return Integer.parseInt(record);
            return 1;
        }
        public void setLanguage(String language)
        {
            setRecord(language, LANGUAGE);
        }
        public String getLanguage()
        {
            String record = getRecord(LANGUAGE);
            if(record != null)
                return record;
            return "1";
        }
        public void setLastTiming(long lastTiming)
        {
            setRecord(Long.toString(lastTiming), LAST_TIMING);
        }
        public long getLastTiming()
        {
            String record = getRecord(LAST_TIMING);
            if(record != null)
                return Long.parseLong(record);
            return new Date().getTime();
        }
        public void setNumberOfTiming(int numberOfTiming)
        {
            setRecord(Integer.toString(numberOfTiming), NUMBER_OF_TIMING);
        }
        public int getNumberOfTiming()
        {
            String record = getRecord(NUMBER_OF_TIMING);
            if(record != null)
                return Integer.parseInt(record);
            return 0;
        }
        public void setLogin(String login)
        {
            setRecord(login, LOGIN);
        }
        public String getLogin()
        {
            String record = getRecord(LOGIN);
            if(!record.equals("null"))
                return record;
            return null;
        }
        public void setPassword(String password)
        {
            setRecord(password, PASSWORD);
        }
        public String getPassword()
        {
            String record = getRecord(PASSWORD);
            if(!record.equals("null"))
                return record;
            return null;
        }
        public void setURL(String url)
        {
            setRecord(url, URL);
        }
        public String getURL()
        {
            String record = getRecord(URL);
            if(!record.equals("null"))
                return record;
            return null;
        }
        public void setText(String text)
        {
            setRecord(text, TEXT);
        }
        public String getText()
        {
            String record = getRecord(TEXT);
            if(!record.equals("null"))
                return record;
            return null;
        }
        private void addRecord(String record)
        {
 		try
                {
			writer.writeUTF(record);
			byte[] data = byteOutputStream.toByteArray();
			rs.addRecord(data, 0, data.length );
			writer.flush();
			byteOutputStream.reset();
		}
		catch( RecordStoreException e ){}
		catch(IOException ioe){}
		re.rebuild();
        }
        private void setRecord(String record, int id)
	{
		try
                {
			writer.writeUTF(record);
			byte[] data = byteOutputStream.toByteArray();
			rs.setRecord(id, data, 0, data.length );
			writer.flush();
			byteOutputStream.reset();
		}
		catch( RecordStoreException e ){}
		catch(IOException ioe){}
		re.rebuild();
        }
        private String getRecord(int id)
	{
            try
            {
                byte[] data = new byte[rs.getRecordSize(id)];
                data = rs.getRecord(id);
                byteInputStream = new ByteArrayInputStream(data);
                reader = new DataInputStream(byteInputStream);
                return reader.readUTF();
            }
            catch( RecordStoreException e ){}
            catch(IOException ioe){}
            return null;
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