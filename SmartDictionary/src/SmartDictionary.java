import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

// TODO: (1.high) Просмотреть публичные методы и переменные, что где используется и написать тудушки с предложениями и замечаниями

//TODO: (2.medium) Стоит перенести все классы в пакет.
// например в org.smdmobile или в org.smdme (от слова javame).
public class SmartDictionary extends MIDlet implements CommandListener
{
        private Text text = new Text();

	private Command exitMIDlet = new Command(text.EXIT, Command.EXIT, 0);
	private Command back = new Command(text.BACK, Command.EXIT, 0);
        private Command backF2 = new Command(text.BACK, Command.EXIT, 0);
	private Command OK = new Command(text.OK, Command.SCREEN, 1);

        private Command next = new Command(text.NEXT, Command.SCREEN, 1);
        private Command Save = new Command(text.SAVE, Command.SCREEN, 1);

        public Form workForm = new Form(text.START);
        public Form addWordForm = new Form(text.ADD_WORD);

        private String[] cgName = {text.DONT_KNOW, text.BAD, text.NORMALLY, text.GOOD, text.VERY_GOOD};
        private TextField tfRus = new TextField(text.ENTER_THE_WORD_ORIGINALLY, "", 20, TextField.ANY);
        private TextField tfEng = new TextField(text.ENTER_TRANSLATION, "", 20, TextField.ANY);
        private ChoiceGroup mycg = new ChoiceGroup(text.KNOWLEDGE, ChoiceGroup.POPUP, cgName, null);

      	private TextField[] tf;
        private StringItem[] siAnswer;
        private StringItem si = new StringItem(text.EMPTY_DICTIONARY,"");

        private Dictionary dictionary;
        private Settings settings = new Settings();

        private int rows[];
        private int wordsN = 1;

        MainForm mf;

	public void startApp() 
	{
		if(settings.getLanguage().equals("null"))
		{
			dictionary = new Dictionary();
		}
		else
		{
			dictionary = new Dictionary(settings.getLanguage());
		}

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
			destroyApp(false);
			notifyDestroyed();
		}
		if(c == back) 
		{
			Display.getDisplay(this).setCurrent(mf);
		}
		if(c == backF2)
		{            
                    Display.getDisplay(this).setCurrent(mf);
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
			Display.getDisplay(this).setCurrent(mf);
		}
	}

	public void workFormInit()
	{
		workForm.addCommand(OK);
		workForm.addCommand(back);
		workForm.setCommandListener(this);
		dictionary = new Dictionary(settings.getLanguage());
		workFormReset();
	}

 	public void workFormReset()
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
        public void workFormAnswer(boolean answer[])
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

	public void addWordFormInit()
	{
		addWordForm.addCommand(back);
		addWordForm.addCommand(Save);
		addWordForm.setCommandListener(this);
		dictionary = new Dictionary(settings.getLanguage());
		addWordFormReset();
	}

	public void addWordFormReset()
	{
		mycg = new ChoiceGroup(text.KNOWLEDGE, ChoiceGroup.POPUP, cgName, null);
                tfRus.delete(0, tfRus.getString().length());
                tfEng.delete(0, tfEng.getString().length());
                addWordForm.deleteAll();
                addWordForm.append(tfRus);
                addWordForm.append(tfEng);
		addWordForm.append(mycg);
	}
        private void goToTheMainForm()
        {
            mf = new MainForm(this);
            Display.getDisplay(this).setCurrent(mf);
        }
}
