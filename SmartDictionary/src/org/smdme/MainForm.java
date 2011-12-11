package org.smdme;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

public class MainForm extends myForm implements CommandListener
{
    private Command choice = new Command(T.CHOICE, Command.SCREEN, 1);
    private Command exitMIDlet = new Command(T.EXIT, Command.EXIT, 0);

    SmartDictionary sd;

    MainForm(SmartDictionary sd)
    {
        this.sd = sd;
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
    protected void drawSomething()
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
    protected void initMainNumber()
    {
        mainNumber = 4;
    }
    public String getMainPath()
    {
        return "/org/images/main/main.png";
    }
    public String getMainName()
    {
        return new Text().MAIN_MENU;
    }
    protected String[] getList()
    {
        String list[] = new String[mainNumber];
        list[0] = T.START;
        list[1] = T.ADD_WORD;
        list[2] = T.DICTIONARY;
        list[3] = T.SETTINGS;
        return list;
    }
    protected String[] getPaths()
    {
        String paths[] = new String[mainNumber];
        String path = "/org/images/main";
        paths[0] = path+"/game.png";
        paths[1] = path+"/newWord.png";
        paths[2] = path+"/dictionary.png";
        paths[3] = path+"/settings.png";
        return paths;
    }
    public void dieIsCast()
    {
        if(mainSelectedRow == 1)
            sd.goToTheWorkForm();
        else if(mainSelectedRow == 2)
            sd.goToTheAddWordForm();
        else if(mainSelectedRow == 3)
            sd.goToTheListForm();
        else if(mainSelectedRow == 4)
            sd.goToTheSettingsForm();
    }
}
