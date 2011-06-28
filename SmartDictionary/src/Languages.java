import javax.microedition.rms.*;
import java.io.*;
import java.util.*;

public class Languages
{
        private RecordStore rs = null;
        private RecordEnumeration re;

        private ByteArrayOutputStream byteOutputStream;
        private DataOutputStream writer;
        private ByteArrayInputStream byteInputStream;
        private DataInputStream reader;
        
        private String language;


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
		else 
                    return null;
	}
	public String getLanguage(int n)
	{
		if (getNumRecords() != 0)
	 	{
			String Language = new String();
			try {
				byte[] data = new byte[rs.getRecordSize(getId(n))];
				data = rs.getRecord(getId(n));
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
        public void newLanguage(String name)
	{
		try{
			writer.writeUTF(name);
			byte[] data = byteOutputStream.toByteArray();
			rs.addRecord( data, 0, data.length );
			writer.flush();
			byteOutputStream.reset();
		}
		catch( RecordStoreException e ){}
		catch(IOException ioe){}
		re.rebuild();
        }
        public void deleteLanguage(int n)
	{
		int id = getId(n);
		try {
			rs.deleteRecord(id);
		}
		catch( RecordStoreException e ){}
		re.rebuild();
        }

        public int getId(int n)
	{
		int id = 0;
		for(int i = 0;i < n; i++ ) {
			try {
				id = re.nextRecordId();
			}
			catch(RecordStoreException e){}
		}
		re.rebuild();
		return id;
        }
        public int getNumRecords() {
		int n = 0;
                try {
			n = rs.getNumRecords();
		}
		catch(RecordStoreException e){}
                return n;
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