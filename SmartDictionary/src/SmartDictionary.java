import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import javax.microedition.rms.*;
import java.io.*;
import java.util.*;

public class SmartDictionary extends MIDlet implements CommandListener
{
	private Command exitMIDlet = new Command("Выход", Command.EXIT, 0);
	private List myList;
	private Command choice = new Command("Выбор", Command.SCREEN, 1);
	private Command back = new Command("Назад", Command.EXIT, 0);
	private Command OK = new Command("OK", Command.SCREEN, 1);
        private Command choice2 = new Command("Опции", Command.SCREEN, 1);
	private Form myForm1 = new Form("Пуск");
	private Form myForm2 = new Form("Добавить новую пару");
	private List myList2;
	private String[] name = { "Пуск", "Добавить новую пару", "Словарь"};
	private String str = "null";
	private StringItem si = new StringItem("", str);
	private TextField tf = new TextField("Введите текст:", "", 20, TextField.ANY);
        private RecordStore rs = null;
        private ByteArrayOutputStream byteOutputStream;
        private DataOutputStream writer;
        private ByteArrayInputStream byteInputStream;
        private DataInputStream reader;
	
	public void startApp() 
	{
                byteOutputStream = new ByteArrayOutputStream();
                writer = new DataOutputStream(byteOutputStream);
		myList = new List("Меню", Choice.IMPLICIT, name, null);
		myList.addCommand(choice);
		myList.addCommand(exitMIDlet);
		myList.setCommandListener(this);
		myForm1.append(tf);
		myForm1.addCommand(OK);
		myForm1.addCommand(back);
		myForm1.setCommandListener(this);
		myForm2.append(si);
		myForm2.addCommand(back);
		myForm2.setCommandListener(this);
        try {
			rs = RecordStore.openRecordStore( "DB", true );
		}
		catch( RecordStoreException e ){}
		Display.getDisplay(this).setCurrent(myList);
	}
	public void pauseApp() {}

	public void destroyApp(boolean unconditional) {}

	public void commandAction(Command c, Displayable s) 
	{
		if(c == exitMIDlet)
		{
			try
			{
				rs.closeRecordStore();
			}
			catch (RecordStoreException e) { }
                        try {
                            writer.close();
                            byteOutputStream.close();
                            reader.close();
                            byteInputStream.close();
                        }
                        catch(IOException ioe){}
			destroyApp(false);
			notifyDestroyed();
		}
		if(c == back) 
		{
			Display.getDisplay(this).setCurrent(myList);
		}
		if(c == OK) 
		{
                        try {
                            writer.writeUTF(tf.getString());
                            byte[] data = byteOutputStream.toByteArray();
                            tf.delete(0, tf.getString().length());
                            try {
                                    rs.addRecord( data, 0, data.length );
                            }
                            catch( RecordStoreException e ){}
                            writer.flush();
                            byteOutputStream.reset();
                        }
                        catch(IOException ioe){}
			Display.getDisplay(this).setCurrent(myList);
		}
		if (c == choice)
		{
			int i = myList.getSelectedIndex();
			if(i == 0)
			{
				Display.getDisplay(this).setCurrent(myForm1);
			}
			if (i == 1)
			{
				str = "\n";
                                String[] sm = getS();
				for(int j = 0;j < getN() ;j++) {
					str = str +Integer.toString(j+1)+" - "+ sm[j] +"\n";
				}
				si.setText(str);
				Display.getDisplay(this).setCurrent(myForm2);
			}
			if (i == 2)
			{
				myList2 = new List("Словарь", Choice.IMPLICIT, getS(), null);
				myList2.addCommand(back);
				myList2.addCommand(choice2);
				myList2.setCommandListener(this);
				Display.getDisplay(this).setCurrent(myList2);
			}
		}
	}
	private String[] getS()
	{
            if (getN() != 0)
            {

                String SM[] = new String[getN()];
                try{
                    for (int k = 1; k <=  rs.getNumRecords(); k++)
                    {
                        byte[] data = new byte[rs.getRecordSize(k)];
                        data = rs.getRecord(k);
                        byteInputStream = new ByteArrayInputStream(data);
                        reader = new DataInputStream(byteInputStream);
                        try {
                            SM[k-1] = reader.readUTF();
                        }
                        catch(IOException ioe){}
                    }
                }
                catch(RecordStoreException e){}
                return SM;
            }
		else {
			String strS[] = new String[2];
			strS[0] = "";
			strS[1] = ""; 
			return strS;
		}
	}
	private int getN()
	{
		int j = 1;
		for (j = 1; ; j++)
		{
			byte[] data = new byte[40];
			try
			{
				rs.getRecord(j, data, 0);
			}
			catch (RecordStoreException e) { break; }
			catch (java.lang.NullPointerException npe) { break; }
		}
		return j - 1;
	}
}