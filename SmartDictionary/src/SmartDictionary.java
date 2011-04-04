import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import javax.microedition.rms.*;
import java.io.*;
import java.util.*;

public class SmartDictionary extends MIDlet implements CommandListener
{
	private Command exitMIDlet = new Command("Выход", Command.EXIT, 0);
	private Command choice = new Command("Выбор", Command.SCREEN, 1);
	private Command back = new Command("Назад", Command.EXIT, 0);
	private Command OK = new Command("OK", Command.SCREEN, 1);
        private Command delete = new Command("Удалить", Command.SCREEN, 1);

	private List myList;
	private List myList2;
	private Form myForm1 = new Form("Пуск");
	private Form myForm2 = new Form("Добавить новую пару");

	private String[] name = { "Пуск", "Добавить новую пару", "Словарь"};
	private StringItem si = new StringItem("", "");
	private TextField tf = new TextField("Введите текст:", "", 20, TextField.ANY);

        private RecordStore rs = null;
        private RecordEnumeration re;

        private ByteArrayOutputStream byteOutputStream;
        private DataOutputStream writer;
        private ByteArrayInputStream byteInputStream;
        private DataInputStream reader;
	
	public void startApp() 
	{
                byteOutputStream = new ByteArrayOutputStream();
                writer = new DataOutputStream(byteOutputStream);

		listInit();
		form1Init();
		form2Init();

        	try {
			rs = RecordStore.openRecordStore("DB", true);
			re = rs.enumerateRecords(null, null, false);
		}
		catch( RecordStoreException e ) {}
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
				re.destroy();
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
				String str = "\n";
                                String[] sm = getS();
				for(int j = 0; j < sm.length; j++) {
					str = str +Integer.toString(j+1)+" - "+ sm[j] +"\n";
				}
				si.setText(str);
				Display.getDisplay(this).setCurrent(myForm2);
			}
			if (i == 2)
			{
				re.rebuild();
				list2Init();
				Display.getDisplay(this).setCurrent(myList2);
			}
		}
                if (c == delete) 
		{
			int id = getId(myList2.getSelectedIndex());
			try {
				rs.deleteRecord(id);
			}
			catch( RecordStoreException e ){}
			re.rebuild();
			list2Init();
			Display.getDisplay(this).setCurrent(myList2);
                }
	}
	private void listInit()
	{
		myList = new List("Меню", Choice.IMPLICIT, name, null);
		myList.addCommand(choice);
		myList.addCommand(exitMIDlet);
		myList.setCommandListener(this);

	}
	private void form1Init()
	{
		myForm1.append(tf);
		myForm1.addCommand(OK);
		myForm1.addCommand(back);
		myForm1.setCommandListener(this);

	}
	private void form2Init()
	{
		myForm2.append(si);
		myForm2.addCommand(back);
		myForm2.setCommandListener(this);
	}
        private void list2Init() 
	{
		myList2 = new List("Словарь", Choice.IMPLICIT, getS(), null);
		myList2.addCommand(back);
		myList2.addCommand(delete);
		myList2.setCommandListener(this);
        }
        private int getId(int n) 
	{
		int id = 0;
		for(int i = 0;i <= n;i++ ) {
			try {
				id = re.nextRecordId();
			}
			catch(RecordStoreException e){}
		}
		re.rebuild();
		return id;
        }
	private String[] getS() 
	{
		int n = 0;
		try {
			n = rs.getNumRecords();
		}
		catch(RecordStoreException e){}
		if (n != 0)
	 	{
			String SM[] = new String[n];
			for (int k = 0; k <  n; k++) {
				try {
					int id = re.nextRecordId();
					byte[] data = new byte[rs.getRecordSize(id)];
					data = rs.getRecord(id);
					byteInputStream = new ByteArrayInputStream(data);
					reader = new DataInputStream(byteInputStream);
					SM[k] = reader.readUTF();
				}
				catch(RecordStoreException e){}
				catch(IOException ioe){}
			}
			re.rebuild();
			return SM;
		}
		else {
			String strS[] = new String[2];
			strS[0] = "";
			strS[1] = ""; 
			return strS;
		}
	}
}