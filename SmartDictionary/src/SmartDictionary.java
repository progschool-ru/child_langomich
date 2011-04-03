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
	private Command back = new Command("Назад", Command.SCREEN, 1);
	private Command OK = new Command("OK", Command.SCREEN, 1);
	private Form myForm1 = new Form("Экран №1");
	private Form myForm2 = new Form("Экран №2");
	private List myList2;
	private String[] name = { "Экран №1", "Экран №2", "Словарь"};
	private String str = "null";
	private StringItem si = new StringItem("", str);
	private TextField tf = new TextField("Введите текст:", "", 20, TextField.ANY);
    private RecordStore rs = null;
	
	public void startApp() 
	{
		myList = new List("Меню", Choice.IMPLICIT, name, null);
		myList.addCommand(choice);
		myList.addCommand(exitMIDlet);
		myList.setCommandListener(this);
		myForm1.append(tf);
		myForm1.addCommand(OK);
		myForm1.addCommand(exitMIDlet);
		myForm1.setCommandListener(this);
		myForm2.append(si);
		myForm2.addCommand(back);
		myForm2.addCommand(exitMIDlet);
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
			destroyApp(false);
			notifyDestroyed();
		}
		if(c == back) 
		{
			Display.getDisplay(this).setCurrent(myList);
		}
		if(c == OK) 
		{
			str = tf.getString();
			tf.delete(0, str.length());
			byte[] data = str.getBytes();
			try {
				rs.addRecord( data, 0, data.length );
			}
			catch( RecordStoreException e ){}
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
				for(int j = 1;;j++) {
					byte[] data = new byte[20];
					try {
						int numBytes = rs.getRecord( j, data, 0 );

					}
					catch( RecordStoreException e ){ break;}
					String st = new String(data);
					str = str +Integer.toString(j)+" - "+ st +"\n";
				}
				si.setText(str);
				Display.getDisplay(this).setCurrent(myForm2);
			}
			if (i == 2)
			{
				myList2 = new List("Экран №3", Choice.IMPLICIT, getS(), null);
				myList2.addCommand(back);
				myList2.addCommand(exitMIDlet);
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
			for (int j = 0; ; j++)
			{
				byte[] data = new byte[20];
				try
				{
					rs.getRecord(j+1,data, 0);
				}
				catch (RecordStoreException e) { break; }
				SM[j] = new String(data);
			}
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
			byte[] data = new byte[20];
			try
			{
				int numBytes = rs.getRecord(j, data, 0);
			}
			catch (RecordStoreException e) { break; }
			catch (java.lang.NullPointerException npe) { break; }
		}
		return j - 1;
	}
  private String[] split(String original,String separator) {
    Vector nodes = new Vector();
    // Parse nodes into vector
    int index = original.indexOf(separator);
    while(index >= 0) {
        nodes.addElement( original.substring(0, index) );
        original = original.substring(index+separator.length());
        index = original.indexOf(separator);
    }
    // Get the last node
    nodes.addElement( original );

     // Create split string array
    String[] result = new String[ nodes.size() ];
    if( nodes.size() > 0 ) {
        for(int loop = 0; loop < nodes.size(); loop++)
        {
            result[loop] = (String)nodes.elementAt(loop);
            System.out.println(result[loop]);
        }

    }
   return result;
}
}