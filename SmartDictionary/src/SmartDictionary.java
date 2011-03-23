import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
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
	private String[] name = {"Экран №1", "Экран №2", "Выход" };
	private Image[] icon;
	private String str = "null";
	private StringItem si = new StringItem("1", str);
	private TextField tf = new TextField("Введите текст:", "", 20, TextField.ANY); 
	
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
		Display.getDisplay(this).setCurrent(myList);
	}
	public void pauseApp() {}

	public void destroyApp(boolean unconditional) {}

	public void commandAction(Command c, Displayable s) 
	{
		if(c == exitMIDlet)
		{
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
				si.setText(str);
				Display.getDisplay(this).setCurrent(myForm2);
			}
			if (i == 2)
			{
				destroyApp(false);
				notifyDestroyed();
			}
		}
	}
}