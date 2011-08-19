import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStoreException;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public abstract class Records
{
        public static final int SINGLE_RECORD = 1;

        private ByteArrayOutputStream byteOutputStream;
        private DataOutputStream writer;
        private ByteArrayInputStream byteInputStream;
        private DataInputStream reader;

        private RecordStore rs = null;
        private RecordEnumeration re;

        protected void recordStoreInit(String name, Filter filter, Ordering ordering)// TODO (2.medium) исправить warning.
		{
                byteOutputStream = new ByteArrayOutputStream();
                writer = new DataOutputStream(byteOutputStream);
        	try {
			rs = RecordStore.openRecordStore(name, true);
			re = rs.enumerateRecords(filter, ordering, false);
		}
		catch( RecordStoreException e ) {}
        }
        protected void addRecord(String record)
        {
            String [] str = new String[1];
            str[0] = record;
            addRecord(str);
        }
        protected void setRecord(String record, int id)
	{
            String [] str = new String[1];
            str[0] = record;
            setRecord(str, id);
        }
		
	public String[] getFullRecord(int id, int columnsNumber)
	{
		String[] record = null;
		if (getNumRecords() != 0)
		{
			record = new String[columnsNumber];
			try
			{
				byte[] data = new byte[rs.getRecordSize(id)];
				data = rs.getRecord(id);
				byteInputStream = new ByteArrayInputStream(data);
				reader = new DataInputStream(byteInputStream);
				for(int j = 0; j < columnsNumber; j++)
					record[j] = reader.readUTF();
			}
			catch(RecordStoreException e ){record = null;}
			catch(IOException ioe){record = null;}
		}
		return record;
	}
	
	public void deleteAllRecords()
	{
		while(getNumRecords() > 0)
		{
			deleteRecord(getId(1));
		}		
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
                catch(RecordStoreException e ){ record = null;}
                catch(IOException ioe){ record = null;}
            }
            return record;
        }
        protected void addRecord(String[] record)
        {
 		try
                {
                    for(int i = 0; i < record.length; i++)
			writer.writeUTF(record[i]);
                    byte[] data = byteOutputStream.toByteArray();
                    rs.addRecord(data, 0, data.length );
                    writer.flush();
                    byteOutputStream.reset();
		}
		catch( RecordStoreException e ){}
		catch(IOException ioe){}
		re.rebuild();
        }
        protected void setRecord(String[] record, int id)
	{
		try
                {
                    for(int i = 0; i < record.length; i++)
			writer.writeUTF(record[i]);
                    byte[] data = byteOutputStream.toByteArray();
                    rs.setRecord(id, data, 0, data.length );
                    writer.flush();
                    byteOutputStream.reset();
		}
		catch( RecordStoreException e ){}
		catch(IOException ioe){}
		re.rebuild();
        }
        protected void deleteRecord(int id)
        {
            try {
                    rs.deleteRecord(id);
            }
            catch( RecordStoreException e ){}
            re.rebuild();
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
                    catch(RecordStoreException e ){records[k] = null;}
                    catch(IOException ioe){records[k] = null;}
                }
                re.rebuild();
            }
            return records;
        }
        public int getId(int row)//TODO: (2.medium) Разобраться с индексами
				//Массив getColumn выдаёт индексы от 0 до n-1, 
				//а этот метод оперирует индесками от 1 до n. 
				//Надо бы привести к единому знаменателю.
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
        public int getNumRecords()
        {
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
