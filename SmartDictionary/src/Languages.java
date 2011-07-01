import javax.microedition.rms.*;
import java.io.*;

public class Languages
{
        private RecordStore rs = null;
        private RecordEnumeration re;

        private ByteArrayOutputStream byteOutputStream;
        private DataOutputStream writer;
        private ByteArrayInputStream byteInputStream;
        private DataInputStream reader;
        
        Languages()
        {
                byteOutputStream = new ByteArrayOutputStream();
                writer = new DataOutputStream(byteOutputStream);
        	try {
			rs = RecordStore.openRecordStore("Languages", true);
			re = rs.enumerateRecords(null, null, false);
		}
		catch( RecordStoreException e ) {}
        }
	public String[] getLanguages()
	{
		if (getNumRecords() != 0)
	 	{
			String Languages[] = new String[getNumRecords()];
			for (int k = 0; k <  getNumRecords(); k++) {
				try {
					int id = re.nextRecordId();
					byte[] data = new byte[rs.getRecordSize(id)];
					data = rs.getRecord(id);
					byteInputStream = new ByteArrayInputStream(data);
					reader = new DataInputStream(byteInputStream);
                                        Languages[k] = reader.readUTF();
				}
				catch(RecordStoreException e){}
				catch(IOException ioe){}
			}
			re.rebuild();
			return Languages;
		}
		else {
                    return null;
                }
	}
	public String getLanguage(int row)
	{
		if (getNumRecords() != 0)
	 	{
			String Language = new String();
			try {
				byte[] data = new byte[rs.getRecordSize(getId(row))];
				data = rs.getRecord(getId(row));
				byteInputStream = new ByteArrayInputStream(data);
				reader = new DataInputStream(byteInputStream);
                                Language = reader.readUTF();
			}
			catch(RecordStoreException e){}
			catch(IOException ioe){}
			re.rebuild();
			return Language;
		}
		else 
                    return null;
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
        public int getNumRecords() {
                return re.numRecords();
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