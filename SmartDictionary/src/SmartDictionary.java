import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import javax.microedition.rms.*;
import java.io.*;
import java.util.*;

public class SmartDictionary extends MIDlet implements CommandListener
{
	private Command exitMIDlet = new Command("�����", Command.EXIT, 0);
	private Command choice = new Command("�����", Command.SCREEN, 1);
	private Command back = new Command("�����", Command.EXIT, 0);
	private Command OK = new Command("OK", Command.SCREEN, 1);
        private Command Save = new Command("���������", Command.SCREEN, 1);
        private Command delete = new Command("�������", Command.SCREEN, 1);

	private List myList;
	private List myList2;
	private Form myForm1 = new Form("����");
	private Form myForm2 = new Form("�������� ����� ����");

        private String[] cgName = {"�� ����", "�����", "���������", "������", "�������"};
        private TextField tfRus = new TextField("������� ����� �� �������:", "", 20, TextField.ANY);
        private TextField tfEng = new TextField("������� ��� �� ����� �� ��������� �����:", "", 20, TextField.ANY);
        private ChoiceGroup mycg = new ChoiceGroup("������� ��, ��� �� ������ ��� �����:", ChoiceGroup.EXCLUSIVE, cgName, null);

        private String[] name = {"����", "�������� ����� ����", "�������"};   
	private StringItem si = new StringItem("", "");
	private TextField tf = new TextField("������� �����:", "", 20, TextField.ANY);

        private Records records;
	
	public void startApp() 
	{
                records = new Records();

		listInit();
		form1Init();
		form2Init();

        	Display.getDisplay(this).setCurrent(myList);
	}
	public void pauseApp() {}

	public void destroyApp(boolean unconditional) {}

	public void commandAction(Command c, Displayable s) 
	{
		if(c == exitMIDlet)
		{
			records.destroy();
			destroyApp(false);
			notifyDestroyed();
		}
		if(c == back) 
		{
			Display.getDisplay(this).setCurrent(myList);
		}
		if(c == OK) 
		{

			Display.getDisplay(this).setCurrent(myList);
		}
                if(c == Save)
		{
                        String str1 = tfRus.getString();
                        String str2 = tfEng.getString();
                        int n = mycg.getSelectedIndex()*2;
                        records.newRecord(str1, str2, n);
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
				
				Display.getDisplay(this).setCurrent(myForm2);
			}
			if (i == 2)
			{
				list2Init();
				Display.getDisplay(this).setCurrent(myList2);
			}
		}
                if (c == delete) 
		{
                        records.deleteRecord(myList2.getSelectedIndex());
			list2Init();
			Display.getDisplay(this).setCurrent(myList2);
                }
	}
	private void listInit()
	{
		myList = new List("����", Choice.IMPLICIT, name, null);
		myList.addCommand(choice);
		myList.addCommand(exitMIDlet);
		myList.setCommandListener(this);
	}
	private void form1Init()
	{
                String str = "��������� �����";
                si.setText(str);
                myForm1.append(si);
		myForm1.append(tf);
		myForm1.addCommand(OK);
		myForm1.addCommand(back);
		myForm1.setCommandListener(this);
	}
	private void form2Init()
	{
                myForm2.append(tfRus);
                myForm2.append(tfEng);
		myForm2.append(mycg);
		myForm2.addCommand(back);
                myForm2.addCommand(Save);
		myForm2.setCommandListener(this);
	}
        private void list2Init() 
	{
                if(records.getNumRecords(records.rs) == 0)
                {
                        String S[] = new String[1];
                        S[0] = "������� ����";
                        myList2 = new List("�������", Choice.IMPLICIT, S, null);
                        myList2.addCommand(back);
                        myList2.setCommandListener(this);
                }
                else
                {
                    myList2 = new List("�������", Choice.IMPLICIT, records.getS(1), null);
                    myList2.addCommand(back);
                    myList2.addCommand(delete);
                    myList2.setCommandListener(this);
                }
        }
}