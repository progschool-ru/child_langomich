import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

public class SmartDictionary extends MIDlet implements CommandListener
{
        private Text text = new Text();

	private Command exitMIDlet = new Command(text.EXIT, Command.EXIT, 0);
	private Command choice = new Command(text.CHOICE, Command.SCREEN, 1);
	private Command back = new Command(text.BACK, Command.EXIT, 0);
        private Command backF2 = new Command(text.BACK, Command.EXIT, 0);
	private Command OK = new Command(text.OK, Command.SCREEN, 1);

        private Command next = new Command(text.NEXT, Command.SCREEN, 1);
        private Command Save = new Command(text.SAVE, Command.SCREEN, 1);
        private Command delete = new Command(text.DELETE, Command.SCREEN, 1);
        private Command ordering1 = new Command(text.ORDERING_1, Command.SCREEN, 1);
        private Command ordering2 = new Command(text.ORDERING_2, Command.SCREEN, 1);

		List mainList;   // TODO: (1.high) make private or public
        Form workForm = new Form(text.START);  // TODO: (1.high) make private or public
        Form addWordForm = new Form(text.ADD_WORD);  // TODO: (1.high) make private or public
	private List dictionaryList;

        private String[] cgName = {text.DONT_KNOW, text.BAD, text.NORMALLY, text.GOOD, text.VERY_GOOD};
        private TextField tfRus = new TextField(text.ENTER_THE_WORD_ORIGINALLY, "", 20, TextField.ANY);
        private TextField tfEng = new TextField(text.ENTER_TRANSLATION, "", 20, TextField.ANY);
        private ChoiceGroup mycg = new ChoiceGroup(text.KNOWLEDGE, ChoiceGroup.POPUP, cgName, null);

        private String[] name = {text.START, text.ADD_WORD, text.DICTIONARY, text.SETTINGS};
	private TextField[] tf;
        private StringItem[] siAnswer;
        private StringItem si = new StringItem(text.EMPTY_DICTIONARY,"");

        private Dictionary dictionary;
        private Languages languages;
        private Settings settings = new Settings();

        private int rows[];
	private int P = 1;
        private int wordsN = 1;

	public void startApp() 
	{
                if(settings.getLanguage().equals("null"))
                    dictionary = new Dictionary();
                dictionary = new Dictionary(settings.getLanguage());
                languages = new Languages();

                    workFormInit();
                    addWordFormInit();
		goToTheMainForm();
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
                           answer[i] = dictionary.answer(rows[i], tf[i].getString());
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
                                if(settings.getLanguage().equals("null"))
                                {

                                }
                                else
                                {
                                    addWordFormReset();
                                    Display.getDisplay(this).setCurrent(addWordForm);
                                }
			}
			if (i == 2)
			{
				dictionaryListInit();
				Display.getDisplay(this).setCurrent(dictionaryList);
			}
                        if (i == 3)
                            goToTheSettingsForm();
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
	}
	private void mainListInit()
	{
		mainList = new List(text.MENU, Choice.IMPLICIT, name, null);
		mainList.addCommand(choice);
		mainList.addCommand(exitMIDlet);
		mainList.setCommandListener(this);
	}
    void workFormInit()// TODO: (1.high) make private or public
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
                rows = new int[wordsN];
                tf = new TextField[wordsN];
                for(int i = 0; i < wordsN; i++) {
                    for(;;) {
                        rows[i]  = dictionary.getRandomRow();
                        boolean f = true;
                        for(int j = 0; j < i; j++)
                            if(rows[i] == rows[j])
                                f = false;
                        if(f)
                            break;
                    }
                    tf[i] = new TextField("  "+dictionary.getCell(rows[i], dictionary.ORIGINAL)+"  -  ", "", 20, TextField.ANY);
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
                    m = text.TRUE;
                else
                    m = text.FALSE;
                siAnswer[i] = new StringItem(dictionary.getCell(rows[i], dictionary.ORIGINAL)+" - "+dictionary.getCell(rows[i], dictionary.TRANSLATION)+"  "+dictionary.getCell(rows[i], dictionary.RATING)  , m);
                workForm.append(siAnswer[i]);
            }
        }
	void addWordFormInit()//TODO: (1.high) make private or public
        {
                addWordForm.addCommand(back);
                addWordForm.addCommand(Save);
		addWordForm.setCommandListener(this);
	}
	private void addWordFormReset()
	{
		mycg = new ChoiceGroup(text.KNOWLEDGE, ChoiceGroup.POPUP, cgName, null);
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
                        String emptyDictionary[] = new String[1];
                        emptyDictionary[0] = text.EMPTY_DICTIONARY;
                        dictionaryList = new List(text.DICTIONARY, Choice.IMPLICIT, emptyDictionary, null);
                }
                else
                {
                    dictionaryList = new List(text.DICTIONARY, Choice.IMPLICIT, dictionary.getFullRecords(), null);
                    dictionaryList.addCommand(delete);
                    dictionaryList.addCommand(ordering1);
                    dictionaryList.addCommand(ordering2);     
                }
                dictionaryList.addCommand(back);
                dictionaryList.setCommandListener(this);
        }
        private void goToTheSettingsForm()
        {
            SettingsForm sf = new SettingsForm(Display.getDisplay(this), mainList);
            Display.getDisplay(this).setCurrent(sf);
        }

		private void goToTheMainForm()
        {
			MainForm mf = new MainForm(this);
			Display.getDisplay(this).setCurrent(mf);
        }
}