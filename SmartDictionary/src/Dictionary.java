import javax.microedition.rms.*;
import java.io.*;
import java.util.*;

public class Dictionary
{
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
                byteOutputStream = new ByteArrayOutputStream();
                writer = new DataOutputStream(byteOutputStream);
        	try {
			rs = RecordStore.openRecordStore("Dicionary", true);
			re = rs.enumerateRecords(null, new Ordering(1), false);
		}
		catch( RecordStoreException e ) {}
        }
        Dictionary(String language)
        {
                this.language = language;
                byteOutputStream = new ByteArrayOutputStream();
                writer = new DataOutputStream(byteOutputStream);
        	try {
                        filter = new Filter(language, 4);
			rs = RecordStore.openRecordStore("Dicionary", true);
			re = rs.enumerateRecords(filter, new Ordering(1), false);
		}
		catch( RecordStoreException e ) {}
        }
	public String[] getS(int p)
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
		if (getNumRecords() != 0)
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
                        Date d =new Date();
			writer.writeUTF(str1);
			writer.writeUTF(str2);
			String str3 = Integer.toString(n);
			writer.writeUTF(str3);
                        writer.writeUTF(language);
                        writer.writeUTF(d.toString());
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
        private void setRecord(String str1, String str2, int p, int id)
	{
		try{
                        Date d =new Date();
			writer.writeUTF(str1);
			writer.writeUTF(str2);
			String str3 = Integer.toString(p);
			writer.writeUTF(str3);
                        writer.writeUTF(language);
                        writer.writeUTF(d.toString());
			byte[] data = byteOutputStream.toByteArray();
			rs.setRecord(id, data, 0, data.length );
			writer.flush();
			byteOutputStream.reset();
		}
		catch( RecordStoreException e ){}
		catch(IOException ioe){}
		re.rebuild();
        }
        public boolean answer(int n, String str)
	{
		int id = getId(n);
                int p = Integer.parseInt(getS(n, 3));
                int pl = 2;
                int mi = 3;
                if(getS(n, 2).equals(str))
                {
                    if(p > 9-pl)
                        p = 9;
                    else
                        p += pl;
                    setRecord(getS(n, 1), getS(n, 2), p, id);
                    re.rebuild();
                    return true;
                }
                else
                {
                    if(p < mi)
                        p = 0;
                    else
                        p -= mi;
                    setRecord(getS(n, 1), getS(n, 2), p, id);
                    re.rebuild();
                    return false;
                }
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
                return re.numRecords();
        }
        public void newOrdering(int p)
        {
            if(p != 1 && p != 2) {
                return;
            }
            try {
                re = rs.enumerateRecords(null, new Ordering(p), false);
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
 //                      reader.close();
 //                       byteInputStream.close();
                }
                catch(IOException ioe){}    
        }
}