import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

//TODO: (2.medium) Между этим классом и формами много связей. Часто каждая форма сама отвечает за то, чтобы сделать другую активной.
//Попытаться уменьшить количество горизонтальных связей между формами: 
//Выделить объект, который отвечает за переходы между формами (SmartDictionary подходит для этой роли)
//Другие формы не должны знать друг о друге, пусть они общаются лишь косвенно, через SmartDictionary.
//Т.е. попытаться совершить переход, изображённый на картинке: http://screencast.com/t/fVVpRZCzYO5
//
//В результате у класса SmartDictionary исчезнут публичые методы и члены.
//У форм публичные методы сведутся к минимуму.


//TODO: (1.high) Стоит перенести все классы в пакет.
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

        private Words words;
        MainForm mf;

	public void startApp() 
	{
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
                        for(int i = 0; i < words.getWordsNumber();i++){
                            words.setTranslation(i, tf[i].getString());
                            System.out.println(i);
                            System.out.println(tf[i].getString());
                            System.out.println(words.getTranslation(i));
                        }
                        workFormAnswer(dictionary.answer(words));
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
		workFormReset();
	}

 	public void workFormReset()//TODO: (1.high) Уже при 50 словах тормозит: долго появляются новые слова и долго выдаётся результат пользователю. Попытаься найти узкое место и устранить его в первую очередь. (по коду разбросаны указания на разные неоптимальности, все сразу их исправлять не нужно - исправим постепенно по мере добирания рук. Начать нужно именно с узкого места).
	{
            dictionary = new Dictionary(settings.getLanguage(), Boolean.TRUE);
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
                tf = new TextField[wordsN];
                words = dictionary.getRandomWords(wordsN);
                for(int i = 0; i < wordsN; i++) 
                {
                    tf[i] = new TextField("  "+words.getOriginal(i)+"  -  ", "", 20, TextField.ANY);
                    workForm.append(tf[i]);
                }
            }
	}
        public void workFormAnswer(Words words)
        {
            workForm.removeCommand(OK);
            workForm.addCommand(next);
            String m = new String();
            siAnswer = new StringItem[wordsN];
            for(int i = 0; i < wordsN; i++) {
                if(words.getAnswer(i))
                    m = text.TRUE;
                else
                    m = text.FALSE;
                siAnswer[i] = new StringItem(words.getOriginal(i) +" - "+words.getTranslation(i) +"  "+words.getRating(i), m);
                workForm.append(siAnswer[i]);
            }
        }

	public void addWordFormInit()
	{
		addWordForm.addCommand(back);
		addWordForm.addCommand(Save);
		addWordForm.setCommandListener(this);
		addWordFormReset();
	}

	public void addWordFormReset()
	{
		dictionary = new Dictionary(settings.getLanguage());
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
