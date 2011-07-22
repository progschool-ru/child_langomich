import javax.microedition.rms.*;
import java.io.*;

public class Languages extends Records
{
        Languages()
        {
                recordStoreInit("Languages", null, null);
        }
	public String[] getLanguages()
	{
            return getColumn(SINGLE_RECORD);
	}
	public String getLanguage(int row)
	{
            return getRecord(getId(row), SINGLE_RECORD);
	}
        public void newLanguage(String language)
	{
                String languages[] = getLanguages();
                for(int i = 0; i < getNumRecords(); i++)
                    if(language.equals(languages[i]))
                        return;
		try{
			writer.writeUTF(language);
			byte[] data = byteOutputStream.toByteArray();
			rs.addRecord( data, 0, data.length );
			writer.flush();
			byteOutputStream.reset();
		}
		catch(RecordStoreException e ){System.out.println("error1 - "+e.getMessage());}
		catch(IOException ioe){System.out.println("error2 - "+ioe.getMessage());}
		re.rebuild();
        }
        public void deleteLanguage(int row)
	{
		int id = getId(row);
		try {
			rs.deleteRecord(id);
		}
		catch( RecordStoreException e ){}
		re.rebuild();
        }
}