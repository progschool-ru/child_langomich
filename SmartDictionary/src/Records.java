import javax.microedition.rms.*;
import java.io.*;

public abstract class Records
{
        public final int SINGLE_RECORD = 1;

        protected ByteArrayOutputStream byteOutputStream;
        protected DataOutputStream writer;
        protected ByteArrayInputStream byteInputStream;
        protected DataInputStream reader;

        protected RecordStore rs = null;
        protected RecordEnumeration re;

        protected void recordStoreInit(String name, Filter filter, Ordering ordering){
                byteOutputStream = new ByteArrayOutputStream();
                writer = new DataOutputStream(byteOutputStream);
        	try {
			rs = RecordStore.openRecordStore(name, true);
			re = rs.enumerateRecords(filter, ordering, false);
		}
		catch( RecordStoreException e ) {}
        }
        public String getRecord(int id, int column)
	{
            String record = null;
            if (getNumRecords() != 0)
	    {
                try
                {
                    byte[] data = new byte[rs.getRecordSize(id)];
                    data = rs.getRecord(id);
                    byteInputStream = new ByteArrayInputStream(data);
                    reader = new DataInputStream(byteInputStream);
                    for(int j = 0; j < column; j++)
                        record = reader.readUTF();
                }
                catch(RecordStoreException e ){}
                catch(IOException ioe){}
            }
            return record;
        }
        public String[] getColumn(int column)
	{
            String records[] = null;
            if (getNumRecords() != 0)
	    {
                records = new String[getNumRecords()];
                for (int k = 0; k <  getNumRecords(); k++) {
                    try {
                        int id = re.nextRecordId();
                        byte[] data = new byte[rs.getRecordSize(id)];
                        data = rs.getRecord(id);
                        byteInputStream = new ByteArrayInputStream(data);
                        reader = new DataInputStream(byteInputStream);
                        for(int j = 0; j < column; j++)
                            records[k] = reader.readUTF();
                    }
                    catch(RecordStoreException e ){}
                    catch(IOException ioe){}
                }
                re.rebuild();
            }
            return records;
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
