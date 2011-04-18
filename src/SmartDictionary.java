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
        private Command backF2 = new Command("�����", Command.EXIT, 0);
	private Command OK = new Command("OK", Command.SCREEN, 1);
        private Command Save = new Command("���������", Command.SCREEN, 1);
        private Command delete = new Command("�������", Command.SCREEN, 1);
        private Command ordering1 = new Command("����. �� 1 ������", Command.SCREEN, 1);
        private Command ordering2 = new Command("����. �� 2 ������", Command.SCREEN, 1);
        private Command settingsSave = new Command("���������", Command.SCREEN, 1);

	private List myList;
	private List myList2;
        private Form settingsForm = new Form("���������");
	private Form myForm1 = new Form("����");
	private Form myForm2 = new Form("�������� ����� ����");

        private String[] wordsNumName = {"1", "2", "3", "4", "5"};
        private ChoiceGroup wordsNum = new ChoiceGroup("���������� ����", ChoiceGroup.POPUP, wordsNumName, null);

        private String[] cgName = {"�� ����", "�����", "���������", "������", "�������"};
        private TextField tfRus = new TextField("������� ����� �� �������:", "", 20, TextField.ANY);
        private TextField tfEng = new TextField("������� ��� �� ����� �� ��������� �����:", "", 20, TextField.ANY);
        private ChoiceGroup mycg = new ChoiceGroup("������� ��, ��� �� ������ ��� �����:", ChoiceGroup.POPUP, cgName, null);

        private String[] name = {"����", "�������� ����� ����", "�������", "��������"};
	private TextField[] tf;
        private StringItem si = new StringItem("������� ����","");

        private Records records;

        private int N[];
	private int P = 1;
        private int wN = 1;
        private int wordsN = 1;

	public void startApp() 
	{
                records = new Records();

		listInit();
                settingsFormInit();
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
		if(c == backF2)
		{            
                    Display.getDisplay(this).setCurrent(myList);
		}
		if(c == OK) 
		{

                        boolean an[] = new boolean[wordsN];
                        for(int i = 0; i < wordsN;i++){
                           an[i] = records.answer(N[i], tf[i].getString());
                        }
                        F1reset();
		}
                if(c == Save)
		{
                        records.newRecord(tfRus.getString(), tfEng.getString(), mycg.getSelectedIndex()*2); 
			Display.getDisplay(this).setCurrent(myList); 
		}
		if (c == choice)
		{
			int i = myList.getSelectedIndex();
			if(i == 0)
			{
                                F1reset();
				Display.getDisplay(this).setCurrent(myForm1);
			}
			if (i == 1)
			{
				F2reset();
				Display.getDisplay(this).setCurrent(myForm2);
			}
			if (i == 2)
			{
				list2Init();
				Display.getDisplay(this).setCurrent(myList2);
			}
                        if (i == 3)
			{

				Display.getDisplay(this).setCurrent(settingsForm);
			}
		}
                if (c == delete) 
		{
                        records.deleteRecord(myList2.getSelectedIndex()+1);
			myList2.delete(myList2.getSelectedIndex());
                        if(records.getNumRecords() == 0)
                            list2Init();
			Display.getDisplay(this).setCurrent(myList2);
                }
                if (c == ordering1)
		{
                    P = 1;
                    records.newOrdering(P);
                    list2Init();
                    Display.getDisplay(this).setCurrent(myList2);
                }
                if (c == ordering2)
		{
                    P = 2;
                    records.newOrdering(P);
                    list2Init();
                    Display.getDisplay(this).setCurrent(myList2);
                }
               if (c ==  settingsSave)
               {
                   wN = wordsNum.getSelectedIndex()+1;
                   System.out.println(wN);
               }
	}
 	private void F1reset()
	{
            myForm1.removeCommand(OK);
            if(records.getNumRecords() == 0)
            {
               myForm1.deleteAll();
               myForm1.append(si);
            }
            else
            {
                myForm1.addCommand(OK);
                myForm1.deleteAll();
                if(wN > records.getNumRecords())
                    wordsN = records.getNumRecords();
                else
                    wordsN = wN;
                N = new int[wordsN];
                tf = new TextField[wordsN];
                for(int i = 0; i < wordsN; i++) {
                    for(;;) {
                        N[i]  = getRandomN();
                        boolean f = true;
                        for(int j = 0; j < i; j++)
                            if(N[i] == N[j])
                                f = false;
                        if(f)
                            break;
                    }
                    tf[i] = new TextField("  "+records.getS(N[i], 1)+"  -  ", "", 20, TextField.ANY);
                    myForm1.append(tf[i]);
                }
            }
	}
	private void F2reset()
	{
		mycg = new ChoiceGroup("������� ��, ��� �� ������ ��� �����:", ChoiceGroup.POPUP, cgName, null);
                tfRus.delete(0, tfRus.getString().length());
                tfEng.delete(0, tfEng.getString().length());
                myForm2.deleteAll();
                myForm2.append(tfRus);
                myForm2.append(tfEng);
		myForm2.append(mycg);
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
		myForm1.addCommand(OK);
		myForm1.addCommand(back);
		myForm1.setCommandListener(this);
	}
	private void form2Init()
	{
		myForm2.addCommand(back);
                myForm2.addCommand(Save);
		myForm2.setCommandListener(this);
	}
        private void settingsFormInit()
        {
                settingsForm.append(wordsNum);
                settingsForm.addCommand(back);
		settingsForm.addCommand(settingsSave);
		settingsForm.setCommandListener(this);
        }
        private void list2Init() 
	{
                if(records.getNumRecords() == 0)
                {
                        String S[] = new String[1];
                        S[0] = "������� ����";
                        myList2 = new List("�������", Choice.IMPLICIT, S, null);
                        myList2.addCommand(back);
                        myList2.setCommandListener(this);
                }
                else
                {

                    myList2 = new List("�������", Choice.IMPLICIT, getS(), null);
                    myList2.addCommand(back);
                    myList2.addCommand(delete);
                    myList2.addCommand(ordering1);
                    myList2.addCommand(ordering2);
                    myList2.setCommandListener(this);
                }
        }
        private int getRandomN()
	{
            if(records.getNumRecords() == 0)
                return 0;
            Random random = new Random ();
            int AllPoint = 0;
            String []S = records.getS(3);
            int []Point = new int[records.getNumRecords()];
            for(int i = 0; i < records.getNumRecords(); i++){
                Point[i] = 10 - Integer.parseInt(S[i]);
                AllPoint += Point[i];
            }
            int r = Math.abs(random.nextInt())%AllPoint;
            System.out.println(r);
            for (int i = 0; i < records.getNumRecords(); i++)
		{
			r = r - Point[i];
			if(r <= 0 )
				return i+1;
		}
            return records.getNumRecords();
        }
        private String[] getS(){
            String []S = new String[records.getNumRecords()];
            String []S1 = records.getS(1);
            String []S2 = records.getS(2);
            String []S3 = records.getS(3);
            for(int i = 0; i < records.getNumRecords(); i++)
                if(P == 1)
                    S[i] = S1[i] +" "+ S2[i] +" "+ S3[i];
                else
                    S[i] = S2[i] +" "+ S1[i] +" "+ S3[i];
            return S;
        }
}