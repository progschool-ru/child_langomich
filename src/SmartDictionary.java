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
        private Command backF2 = new Command("Назад", Command.EXIT, 0);
	private Command OK = new Command("OK", Command.SCREEN, 1);
        private Command Save = new Command("Сохранить", Command.SCREEN, 1);
        private Command delete = new Command("Удалить", Command.SCREEN, 1);

	private List myList;
	private List myList2;
	private Form myForm1 = new Form("Пуск");
	private Form myForm2 = new Form("Добавить новую пару");

        private String[] cgName = {"Не знаю", "Плохо", "Нормально", "Хорошо", "Отлично"};
        private TextField tfRus = new TextField("Введите слово на русском:", "", 20, TextField.ANY);
        private TextField tfEng = new TextField("Введите это же слово на изучаемом языке:", "", 20, TextField.ANY);
        private ChoiceGroup mycg = new ChoiceGroup("Оцените то, как вы знаете это слово:", ChoiceGroup.EXCLUSIVE, cgName, null);

        private String[] name = {"Пуск", "Добавить новую пару", "Словарь"};   
	private TextField tf = new TextField("Введите текст:", "", 20, TextField.ANY);
        private StringItem si = new StringItem("Словарь пуст","");

        private Records records;

        private int N;
	
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
		if(c == backF2)
		{            
                    Display.getDisplay(this).setCurrent(myList);
		}
		if(c == OK) 
		{
                        boolean an = records.answer(N, tf.getString());
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
		}
                if (c == delete) 
		{
                        records.deleteRecord(myList2.getSelectedIndex()+1);
			myList2.delete(myList2.getSelectedIndex());
			Display.getDisplay(this).setCurrent(myList2);
                }
	}
 	private void F1reset()
	{
            if(records.getNumRecords() == 0)
            {
               myForm1.deleteAll();
               myForm1.append(si);
            }
            else
            {
                N  = getRandomN();
                tf = new TextField("  "+records.getS(N, 1)+"  -  ", "", 20, TextField.ANY);
                myForm1.deleteAll();
		myForm1.append(tf);
            }
	}
	private void F2reset()
	{
		mycg = new ChoiceGroup("Оцените то, как вы знаете это слово:", ChoiceGroup.EXCLUSIVE, cgName, null);
                tfRus.delete(0, tfRus.getString().length());
                tfEng.delete(0, tfEng.getString().length());
                myForm2.deleteAll();
                myForm2.append(tfRus);
                myForm2.append(tfEng);
		myForm2.append(mycg);
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
        private void list2Init() 
	{
                if(records.getNumRecords() == 0)
                {
                        String S[] = new String[1];
                        S[0] = "Словарь пуст";
                        myList2 = new List("Словарь", Choice.IMPLICIT, S, null);
                        myList2.addCommand(back);
                        myList2.setCommandListener(this);
                }
                else
                {
                    myList2 = new List("Словарь", Choice.IMPLICIT, getS(), null);
                    myList2.addCommand(back);
                    myList2.addCommand(delete);
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
                S[i] = S1[i] +" "+ S2[i] +" "+ S3[i];
            return S;
        }
}