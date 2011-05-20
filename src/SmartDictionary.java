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
        private Command backToSet = new Command("Назад", Command.EXIT, 0);
	private Command OK = new Command("OK", Command.SCREEN, 1);
        private Command newLen = new Command("Новый язык", Command.SCREEN, 1);
        private Command next = new Command("Продолжить", Command.SCREEN, 1);
        private Command Save = new Command("Сохранить", Command.SCREEN, 1);
        private Command delete = new Command("Удалить", Command.SCREEN, 1);
        private Command ordering1 = new Command("Сорт. по 1 записи", Command.SCREEN, 1);
        private Command ordering2 = new Command("Сорт. по 2 записи", Command.SCREEN, 1);
        private Command settingsSave = new Command("Сохранить", Command.SCREEN, 1);

        private Command saveNewLen = new Command("Сохранить", Command.SCREEN, 1);
        private Command cancel = new Command("Отмена", Command.EXIT, 0);

	private List myList;
	private List myList2;
        private Form settingsForm = new Form("Настройки");
 //       private Form settingsForm2 = new Form("Настройки языка");
	private Form myForm1 = new Form("Пуск");
	private Form myForm2 = new Form("Добавить новую пару");

        private TextBox nLen;

        private String[] wordsNumName = {"1", "2", "3", "4", "5"};
        private ChoiceGroup wordsNum = new ChoiceGroup("Количество слов", ChoiceGroup.POPUP, wordsNumName, null);
        private ChoiceGroup recordsList;

        private String[] cgName = {"Не знаю", "Плохо", "Нормально", "Хорошо", "Отлично"};
        private TextField tfRus = new TextField("Введите слово на русском:", "", 20, TextField.ANY);
        private TextField tfEng = new TextField("Введите это же слово на изучаемом языке:", "", 20, TextField.ANY);
        private ChoiceGroup mycg = new ChoiceGroup("Оцените то, как вы знаете это слово:", ChoiceGroup.POPUP, cgName, null);

        private String[] name = {"Пуск", "Добавить новую пару", "Словарь", "Настрйки"};
	private TextField[] tf;
        private StringItem[] siAnswer;
        private StringItem si = new StringItem("Словарь пуст","");

        private Records records;

        private int N[];
	private int P = 1;
        private int wN = 1;
        private int wordsN = 1;
        private String language = "english";
        private StringItem wns = new StringItem(Integer.toString(wN),"");
        private StringItem lns = new StringItem(language,"");

	public void startApp() 
	{
                records = new Records(language);

		listInit();
                settingsFormInit();
		form1Init();
		form2Init();
                newLenInit();

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
                        boolean answer[] = new boolean[wordsN];
                        for(int i = 0; i < wordsN;i++){
                           answer[i] = records.answer(N[i], tf[i].getString());
                        }
                        F1_answer(answer);
		}
		if(c == next)
		{
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
                                settingsFormReset();
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
                   language = records.getList()[recordsList.getSelectedIndex()];
                   records = new Records(language);
                   Display.getDisplay(this).setCurrent(myList);
               }
               if (c ==  newLen)
               {
                    newLenReset();
                    Display.getDisplay(this).setCurrent(nLen);  
               }
               if (c ==  cancel)
               {
                    Display.getDisplay(this).setCurrent(settingsForm);
               }
               if (c ==  saveNewLen)
               {
                    language = nLen.getString();
                    records = new Records(language);
                    settingsFormReset();
                    Display.getDisplay(this).setCurrent(settingsForm);
               }
	}
 	private void F1reset()
	{
            myForm1.removeCommand(OK);
            myForm1.removeCommand(next);
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
        private void F1_answer(boolean answer[]) {
            myForm1.removeCommand(OK);
            myForm1.addCommand(next);
            String m = new String();
            siAnswer = new StringItem[wordsN];
            for(int i = 0; i < wordsN; i++) {
                if(answer[i])
                    m = "Правильно";
                else
                    m = "Ошибка";
                siAnswer[i] = new StringItem(records.getS(N[i], 1)+" - "+records.getS(N[i], 2)+"  "+records.getS(N[i], 3)  , m);
                myForm1.append(siAnswer[i]);
            }
        }
	private void F2reset()
	{
		mycg = new ChoiceGroup("Оцените то, как вы знаете это слово:", ChoiceGroup.POPUP, cgName, null);
                tfRus.delete(0, tfRus.getString().length());
                tfEng.delete(0, tfEng.getString().length());
                myForm2.deleteAll();
                myForm2.append(tfRus);
                myForm2.append(tfEng);
		myForm2.append(mycg);
	}
	private void settingsFormReset()
	{
                settingsForm.deleteAll();
                wns = new StringItem(Integer.toString(wN),"");
                lns = new StringItem(language,"");
                settingsForm.append(wordsNum);
                settingsForm.append(wns);
                recordsList = new ChoiceGroup("Язык", ChoiceGroup.POPUP, records.getList(), null);
                settingsForm.append(recordsList);
                settingsForm.append(lns);
	}
        private void newLenReset(){
                nLen.delete(0, nLen.getString().length());
        }
        private void newLenInit(){
                nLen = new TextBox("Назовите новый язык:", "", 20, TextField.ANY);
 		nLen.addCommand(cancel);
		nLen.addCommand(saveNewLen);
		nLen.setCommandListener(this);
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
	private void form2Init(){
                myForm2.addCommand(back);
                myForm2.addCommand(Save);
		myForm2.setCommandListener(this);
	}
        private void settingsFormInit()
        {
                settingsFormReset();
                settingsForm.addCommand(back);
		settingsForm.addCommand(settingsSave);
                settingsForm.addCommand(newLen);
		settingsForm.setCommandListener(this);
        }
 /*       private void settingsForm2Init()
        {
                settingsForm2.append(recordsList);
                settingsForm2.addCommand(backToSet);
		settingsForm2.setCommandListener(this);
        } */
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