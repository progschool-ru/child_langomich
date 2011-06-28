import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

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

	private List mainList;
        private Form workForm = new Form("Пуск");
        private Form addWordForm = new Form("Добавить новую пару");
	private List dictionaryList;
        private Form settingsForm = new Form("Настройки");
//        private Form settingsForm2 = new Form("Настройки языка");
	
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

        private Dictionary dictionary;
        private Languages languages;
        private Settings settings = new Settings();;

        private int N[];
	private int P = 1;
        private int wordsN = 1;
        private StringItem wns = new StringItem(Integer.toString(settings.getNumberOfWords()),"");
        private StringItem lns = new StringItem(settings.getLanguage(),"");

	public void startApp() 
	{
                dictionary = new Dictionary(settings.getLanguage());
                languages = new Languages();
                if(languages.getLanguages() == null)
                   languages.newLanguage(settings.getLanguage());
		mainListInit();
                    workFormInit();
                    addWordFormInit();
                    settingsFormInit();
                        newLenInit();

                Display.getDisplay(this).setCurrent(mainList);
	}
	public void pauseApp() {}

	public void destroyApp(boolean unconditional) {}

	public void commandAction(Command c, Displayable s) 
	{
		if(c == exitMIDlet)
		{
			dictionary.destroy();
                        languages.destroy();
			destroyApp(false);
			notifyDestroyed();
		}
		if(c == back) 
		{
			Display.getDisplay(this).setCurrent(mainList);
		}
		if(c == backF2)
		{            
                    Display.getDisplay(this).setCurrent(mainList);
		}
		if(c == OK) 
		{
                        boolean answer[] = new boolean[wordsN];
                        for(int i = 0; i < wordsN;i++){
                           answer[i] = dictionary.answer(N[i], tf[i].getString());
                        }
                        workFormAnswer(answer);
		}
		if(c == next)
		{
                        workFormReset();
		}
                if(c == Save)
		{
                        dictionary.newRecord(tfRus.getString(), tfEng.getString(), mycg.getSelectedIndex()*2);
			Display.getDisplay(this).setCurrent(mainList);
		}
		if (c == choice)
		{
			int i = mainList.getSelectedIndex();
			if(i == 0)
			{
                                workFormReset();
				Display.getDisplay(this).setCurrent(workForm);
			}
			if (i == 1)
			{
				addWordFormReset();
				Display.getDisplay(this).setCurrent(addWordForm);
			}
			if (i == 2)
			{
				dictionaryListInit();
				Display.getDisplay(this).setCurrent(dictionaryList);
			}
                        if (i == 3)
			{
                                settingsFormReset();
				Display.getDisplay(this).setCurrent(settingsForm);
			}
		}
                if (c == delete) 
		{
                        dictionary.deleteRecord(dictionaryList.getSelectedIndex()+1);
			dictionaryList.delete(dictionaryList.getSelectedIndex());
                        if(dictionary.getNumRecords() == 0)
                            dictionaryListInit();
			Display.getDisplay(this).setCurrent(dictionaryList);
                }
                if (c == ordering1)
		{
                    P = 1;
                    dictionary.newOrdering(P);
                    dictionaryListInit();
                    Display.getDisplay(this).setCurrent(dictionaryList);
                }
                if (c == ordering2)
		{
                    P = 2;
                    dictionary.newOrdering(P);
                    dictionaryListInit();
                    Display.getDisplay(this).setCurrent(dictionaryList);
                }
               if (c ==  settingsSave)
               {
                   settings.setNumberOfWords(wordsNum.getSelectedIndex()+1);
                   settings.setLanguage(languages.getLanguages()[recordsList.getSelectedIndex()]);
                   dictionary = new Dictionary(settings.getLanguage());
                   Display.getDisplay(this).setCurrent(mainList);
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
                    settings.setLanguage(nLen.getString());
                    languages.newLanguage(settings.getLanguage());
                    dictionary = new Dictionary(settings.getLanguage());
                    settingsFormReset();
                    Display.getDisplay(this).setCurrent(settingsForm);
               }
	}
	private void mainListInit()
	{
		mainList = new List("Меню", Choice.IMPLICIT, name, null);
		mainList.addCommand(choice);
		mainList.addCommand(exitMIDlet);
		mainList.setCommandListener(this);
	}
        private void workFormInit()
	{
		workForm.addCommand(OK);
		workForm.addCommand(back);
		workForm.setCommandListener(this);
	}
 	private void workFormReset()
	{
            workForm.removeCommand(OK);
            workForm.removeCommand(next);
            if(dictionary.getNumRecords() == 0)
            {
               workForm.deleteAll();
               workForm.append(si);
            }
            else
            {
                workForm.addCommand(OK);
                workForm.deleteAll();
                if(settings.getNumberOfWords() > dictionary.getNumRecords())
                    wordsN = dictionary.getNumRecords();
                else
                    wordsN = settings.getNumberOfWords();
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
                    tf[i] = new TextField("  "+dictionary.getS(N[i], 1)+"  -  ", "", 20, TextField.ANY);
                    workForm.append(tf[i]);
                }
            }
	}
        private void workFormAnswer(boolean answer[])
        {
            workForm.removeCommand(OK);
            workForm.addCommand(next);
            String m = new String();
            siAnswer = new StringItem[wordsN];
            for(int i = 0; i < wordsN; i++) {
                if(answer[i])
                    m = "Правильно";
                else
                    m = "Ошибка";
                siAnswer[i] = new StringItem(dictionary.getS(N[i], 1)+" - "+dictionary.getS(N[i], 2)+"  "+dictionary.getS(N[i], 3)  , m);
                workForm.append(siAnswer[i]);
            }
        }
	private void addWordFormInit()
        {
                addWordForm.addCommand(back);
                addWordForm.addCommand(Save);
		addWordForm.setCommandListener(this);
	}
	private void addWordFormReset()
	{
		mycg = new ChoiceGroup("Оцените то, как вы знаете это слово:", ChoiceGroup.POPUP, cgName, null);
                tfRus.delete(0, tfRus.getString().length());
                tfEng.delete(0, tfEng.getString().length());
                addWordForm.deleteAll();
                addWordForm.append(tfRus);
                addWordForm.append(tfEng);
		addWordForm.append(mycg);
	}
        private void dictionaryListInit()
	{
                if(dictionary.getNumRecords() == 0)
                {
                        String S[] = new String[1];
                        S[0] = "Словарь пуст";
                        dictionaryList = new List("Словарь", Choice.IMPLICIT, S, null);
                        dictionaryList.addCommand(back);
                        dictionaryList.setCommandListener(this);
                }
                else
                {
                    dictionaryList = new List("Словарь", Choice.IMPLICIT, getS(), null);
                    dictionaryList.addCommand(back);
                    dictionaryList.addCommand(delete);
                    dictionaryList.addCommand(ordering1);
                    dictionaryList.addCommand(ordering2);
                    dictionaryList.setCommandListener(this);
                }
        }
        private void settingsFormInit()
        {
                settingsFormReset();
                settingsForm.addCommand(back);
		settingsForm.addCommand(settingsSave);
                settingsForm.addCommand(newLen);
		settingsForm.setCommandListener(this);
        }
        private void settingsFormReset()
	{
                settingsForm.deleteAll();
                wns = new StringItem(Integer.toString(settings.getNumberOfWords()),"");
                lns = new StringItem(settings.getLanguage(),"");
                settingsForm.append(wordsNum);
                settingsForm.append(wns);
                recordsList = new ChoiceGroup("Язык", ChoiceGroup.POPUP, languages.getLanguages(), null);
                settingsForm.append(recordsList);
                settingsForm.append(lns);
	}
        private void newLenReset()
        {
                nLen.delete(0, nLen.getString().length());
        }
        private void newLenInit()
        {
                nLen = new TextBox("Назовите новый язык:", "", 20, TextField.ANY);
 		nLen.addCommand(cancel);
		nLen.addCommand(saveNewLen);
		nLen.setCommandListener(this);
        }
        private int getRandomN()
	{
            if(dictionary.getNumRecords() == 0)
                return 0;
            Random random = new Random ();
            int AllPoint = 0;
            String []S = dictionary.getS(3);
            int []Point = new int[dictionary.getNumRecords()];
            for(int i = 0; i < dictionary.getNumRecords(); i++){
                Point[i] = 10 - Integer.parseInt(S[i]);
                AllPoint += Point[i];
            }
            int r = Math.abs(random.nextInt())%AllPoint;
            System.out.println(r);
            for (int i = 0; i < dictionary.getNumRecords(); i++)
		{
			r = r - Point[i];
			if(r <= 0 )
				return i+1;
		}
            return dictionary.getNumRecords();
        }
        private String[] getS(){
            String []S = new String[dictionary.getNumRecords()];
            String []S1 = dictionary.getS(1);
            String []S2 = dictionary.getS(2);
            String []S3 = dictionary.getS(3);
            for(int i = 0; i < dictionary.getNumRecords(); i++)
                if(P == 1)
                    S[i] = S1[i] +" "+ S2[i] +" "+ S3[i];
                else
                    S[i] = S2[i] +" "+ S1[i] +" "+ S3[i];
            return S;
        }
}