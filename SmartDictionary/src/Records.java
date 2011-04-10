import javax.microedition.rms.*;
import java.io.*;
import java.util.*;

public class Records
{
        private RecordStore rs = null;
        private RecordEnumeration re;

        private ByteArrayOutputStream byteOutputStream;
        private DataOutputStream writer;
        private ByteArrayInputStream byteInputStream;
        private DataInputStream reader;

        Records()
        {
                byteOutputStream = new ByteArrayOutputStream();
                writer = new DataOutputStream(byteOutputStream);
        	try {
			rs = RecordStore.openRecordStore("DB", true);
			re = rs.enumerateRecords(null, null, false);
		}
		catch( RecordStoreException e ) {}  
        }
	public String[] getS(int p)
	{
		if (getNumRecords(rs) != 0)
	 	{
			String S[] = new String[getNumRecords(rs)];
			for (int k = 0; k <  getNumRecords(rs); k++) {
				try {
					int id = re.nextRecordId();
					byte[] data = new byte[rs.getRecordSize(id)];
					data = rs.getRecord(id);
					byteInputStream = new ByteArrayInputStream(data);
					reader = new DataInputStream(byteInputStream);
                                        for(int j = 0; j < p; j++)
                                            S[k] = reader.readUTF();
				}
				catch(RecordStoreException e){}
				catch(IOException ioe){}
			}
			re.rebuild();
			return S;
		}
		else {
			String S[] = new String[1];
			return null;
		}
	}
	public String getS(int n, int p)
	{
		if (getNumRecords(rs) != 0)
	 	{
			String S = new String();
			try {
				byte[] data = new byte[rs.getRecordSize(getId(n))];
				data = rs.getRecord(getId(n));
				byteInputStream = new ByteArrayInputStream(data);
				reader = new DataInputStream(byteInputStream);
                                for(int j = 0; j < p; j++)
                                        S = reader.readUTF();
			}
			catch(RecordStoreException e){}
			catch(IOException ioe){}
			re.rebuild();
			return S;
		}
		else {
			String S = new String();
			return S;
		}
	}
        public void newRecord(String str1, String str2, int n)
	{
		try{
			writer.writeUTF(str1);
			writer.writeUTF(str2);
			String str3 = Integer.toString(n);
			writer.writeUTF(str3);
			byte[] data = byteOutputStream.toByteArray();
			rs.addRecord( data, 0, data.length );
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
        private int getId(int n) 
	{
		int id = 0;
		for(int i = 0;i <= n; i++ ) {
			try {
				id = re.nextRecordId();
			}
			catch(RecordStoreException e){}
		}
		re.rebuild();
		return id;
        }
        private int getNumRecords(RecordStore rs) {
		int n = 0;
                try {
			n = rs.getNumRecords();
		}
		catch(RecordStoreException e){}
                return n;
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
 //                      reader.close();
 //                       byteInputStream.close();
                }
                catch(IOException ioe){}    
        }
}