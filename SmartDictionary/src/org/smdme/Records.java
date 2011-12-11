package org.smdme;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStoreException;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

//TODO: (3.low) Где-то уже было сказано, что классы Dictionary и Languages должны предоставлять более
// высокоуровневый интерфейс по работе со словами и языками, скрывая детали устройства класса Records.
// Кажется, что такой интерфейс у них будет сильно отличаться друг от друга и от класса Records.
// Поэтому я бы попробовал вовсе отказаться от наследования и использовать Records, как член классов
// Dictionary, Languages, Settings. 
//
// Тогда у Records останутся публичные методы, они будут представлять его интерфейс.
// Глядя на такой интерфейс, можно будет думать о том, как сделать его проще и удобнее.
// Просто сейчас часть методов public, а часть protected и не всегда очевидно, 
// какие из них являются интерфейсом, а какие технической необходимостью.
//
// У классов Dictionary, Languages также сформируется свой интерфейс,
// действующий не понятиями записей и их номеров, а на понятиями слов и языков.
//
//
//После того, как это будет сделано, 
// нужно будет оценить количество выполняющихся вхолостую циклов и шагов.
// и попытаться уменьшить их число.
// Наши цели здесь: 
// - желательно, чтобы сложность операций была не более, чем линейна по отношению к количеству слов в словарях
// - сложность не должна быть более, чем квадратичной.

public abstract class Records
{
        public static final int SINGLE_RECORD = 1;

        private ByteArrayOutputStream byteOutputStream;
        private DataOutputStream writer;
        private ByteArrayInputStream byteInputStream;
        private DataInputStream reader;

        private RecordStore rs = null;
        private RecordEnumeration re;

        protected void recordStoreInit(String name, Filter filter, Ordering ordering)// TODO: (2.medium) исправить warning связанный с internal параметром в публичном методе.
		{
			//System.out.println("Init records"); //TODO: (2.medium) раскомментировать эту строчку. Будет видно, что объекты Records создаются чаще, чем это необходимо. Попробовать обойтись меньшим количеством инициализаций. Также, наверное, стоит добиться того, чтобы каждый инициализированный объект должным образом деактивировался.
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
		
	protected String[] getFullRecord(int id, int columnsNumber)
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
	protected String[][] getAllFullRecords()
	{
            String records[][] = null;
            if (getNumRecords() != 0)
	    {
                records = new String[getNumRecords()][];
                for (int k = 0; k <  getNumRecords(); k++) {
                    records[k] = new String[5];
                    try {
                        int id = re.nextRecordId();
                        byte[] data = new byte[rs.getRecordSize(id)];
                        data = rs.getRecord(id);
                        byteInputStream = new ByteArrayInputStream(data);
                        reader = new DataInputStream(byteInputStream);
                        for(int j = 0; j < 5; j++)
                            records[k][j] = reader.readUTF();
                    }
                    catch(RecordStoreException e ){records[k] = null;}
                    catch(IOException ioe){records[k] = null;}
                }
                re.rebuild();
            }
            return records;
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
