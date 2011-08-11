import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;

public class MainForm extends myForm implements CommandListener
{
	private Command choice = new Command(text.CHOICE, Command.SCREEN, 1);
    private Command exitMIDlet = new Command(text.EXIT, Command.EXIT, 0);

    SmartDictionary sd;

    MainForm(SmartDictionary sd)
    {
        this.sd = sd;
        this.mainDisplay = Display.getDisplay(sd);
        this.addCommand(exitMIDlet);
        this.addCommand(choice);
        this.setCommandListener(this);
    }
    public void commandAction(Command c, Displayable s)
    {
        if(c == choice)
            forward();
        if(c == exitMIDlet)
        {
            sd.destroyApp(false);
            sd.notifyDestroyed();
        }
    }
    public void drawSomething()
    {
        drawMenu();
    }
    protected void up()
    {
        if(mainSelectedRow!=1)
            mainSelectedRow--;
        else
            mainSelectedRow = mainNumber;
        repaint();
    }
    protected void down()
    {
        if(mainSelectedRow != mainNumber)
            mainSelectedRow++;
        else
            mainSelectedRow=1;
        repaint();
    }
    protected void forward()
    {
        dieIsCast();
        repaint();
    }
    protected void back()
    {

    }
    public void mainNumber() //TODO: (2.medium) Переименовать в resetMainNumber или setDefaultMainNumber, если оно где-то может измениться. Если же это число не может измениться, то этот метод должен быть приватным или защищённым и называться initMainNumber.
    {
        mainNumber = 4;
    }
    public String getMainPath()
    {
        return "/images/main/main.png";
    }
    public String getMainName()
    {
        return "Главное меню";
    }
    protected String[] getList()
    {
        String list[] = new String[mainNumber];
        list[0] = text.START;
        list[1] = text.ADD_WORD;
        list[2] = text.DICTIONARY;
        list[3] = text.SETTINGS;
        return list;
    }
    protected String[] getPaths()
    {
        String paths[] = new String[mainNumber];
        String path = "/images/main";
        paths[0] = path+"/game.png";
        paths[1] = path+"/newWord.png";
        paths[2] = path+"/dictionary.png";
        paths[3] = path+"/settings.png";
        return paths;
    }
    public void dieIsCast()
    {
        if(mainSelectedRow == 1)
        {
            sd.workFormInit();

            mainDisplay.setCurrent(sd.workForm);
        }
        else if(mainSelectedRow == 2)
        {
            sd.addWordFormInit();
            mainDisplay.setCurrent(sd.addWordForm);
        }
        else if(mainSelectedRow == 3)
        {
            ListForm lf = new ListForm(mainDisplay, this);
            mainDisplay.setCurrent(lf);
        }
        else if(mainSelectedRow == 4)
        {
            SettingsForm sf = new SettingsForm(mainDisplay, this);
            mainDisplay.setCurrent(sf);
        }
    }
}
