package org.smdme;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

public class SettingsForm extends myForm implements CommandListener
{
    private Command choice = new Command(T.CHOICE, Command.SCREEN, 1);
    private Command back = new Command(T.BACK, Command.EXIT, 0);
    private Language[] smallMenuLanguages;

    SmartDictionary sd;

    SettingsForm(SmartDictionary sd)
    {
        this.sd = sd;
        this.addCommand(back);
        this.addCommand(choice);
        this.setCommandListener(this);
    }

    public void commandAction(Command c, Displayable s)
    {
        if(c == back)
            back();
        if(c == choice)
            forward();
    }

    protected void up()
    {
        if(mainButtonIsPressed)
        {
            if(selectedRow!=1)
                selectedRow--;
            else
                selectedRow = number;
        }
        else
        {
            if(mainSelectedRow!=1)
                mainSelectedRow--;
            else
                mainSelectedRow = mainNumber;
        }
        repaint();
    }
    protected void down()
    {
        if(mainButtonIsPressed)
        {
            if(selectedRow != number)
                selectedRow++;
            else
                selectedRow=1;
        }
        else
        {
            if(mainSelectedRow != mainNumber)
                mainSelectedRow++;
            else
                mainSelectedRow=1;
        }
        repaint();
    }
    protected void forward()
    {
        if(mainButtonIsPressed)
        {
            dieIsCast();
            mainButtonIsPressed = false;
        }
        else
        {
            smallMenuInit();
            mainButtonIsPressed = true;
        }
        repaint();
    }
    protected void back()
    {
        if(mainButtonIsPressed)
            mainButtonIsPressed = false;
        else
            sd.goToTheMainForm();
        repaint();
    }
	
    protected void initMainNumber()
    {
        mainNumber = 5;
    }
	
    protected void drawSomething()
    {
        drawMenu();
        if(mainButtonIsPressed)
            drawSmallMenu();
    }

    protected String[] getList()
    {
        String list[] = new String[mainNumber];
        list[0] = T.NUMBER_OF_WORDS+"  "+Integer.toString(sd.settings.getNumberOfWords());
		
        Languages languages = new Languages();
        Language language = languages.getLanguageById(sd.settings.getLanguage());
        languages.destroy();

        list[1] = T.LANGUAGE + "  " + (language == null ? null : language.getName());
        list[2] = T.LOGIN+"  "+sd.settings.getLogin();
        list[3] = T.URL+"  "+sd.settings.getURL();
        list[4] = T.TIMING;
        return list;
    }
	
    protected String[] getPaths()
    {
        String paths[] = new String[mainNumber];
        String path = "/org/images/settings";
        paths[0] = path+"/word.png";
        paths[1] = path+"/lang.png";
        paths[2] = path+"/login.png";
        paths[3] = path+"/url.png";
        paths[4] = path+"/timing.png";
        return paths;
    }
	
    protected String getMainPath()
    {
        return "/org/images/main/settings.png";
    }
	
    protected String getMainName()
    {
        return T.SETTINGS;
    }
 
    private void smallMenuInit()
    {
        selectedRow = 1;
        indent = mainSelectedRow*size-size/4;
        if(mainSelectedRow == 1)
        {
            String str[] = {"1", "2", "3", "4", "5"};
            smallMenuList = str;
        }
        else if(mainSelectedRow == 2)
        {
            int i = 0;

            Languages languages = new Languages();
            smallMenuLanguages = languages.getLanguages();
            languages.destroy();

            if(smallMenuLanguages != null)
            {
                    smallMenuList = new String[smallMenuLanguages.length+1];
                    for(i = 0; i < smallMenuLanguages.length; i++)
                            smallMenuList[i] = smallMenuLanguages[i].getName();
            }
            else
                smallMenuList = new String[1];
            smallMenuList[i]= T.NEW_LANGUAGE;
        }
        else if(mainSelectedRow == 3)
        {
            smallMenuList = new String[2];
            smallMenuList[0]=T.ENTER_LOGIN;
            smallMenuList[1]=T.ENTER_PASSWORD;
        }
        else if(mainSelectedRow == 5)
        {
            String str[]= new String[1];
            str[0] = T.START;
            smallMenuList = str;
        }
        else
        {
            String str[]= new String[1];
            str[0] = T.ENTER_URL;
            smallMenuList = str;
        }
        number = smallMenuList.length; 
    }
    private void dieIsCast()
    {
        if(mainSelectedRow == 1)
        {
            sd.settings.setNumberOfWords(Integer.parseInt(smallMenuList[selectedRow-1]));
        }
        else if(mainSelectedRow == 2)
        {
            if(selectedRow == number)
                sd.goToTheTextBox(T.NEW_LANGUAGE);
            else
                sd.settings.setLanguage(smallMenuLanguages[selectedRow-1].getId());
        }
        else if(mainSelectedRow == 3)
        {
            if(selectedRow == 1)
                sd.goToTheTextBox(T.ENTER_NEW_LOGIN);
            else
                sd.goToTheTextBox(T.ENTER_NEW_PASSWORD);
        }
        else if(mainSelectedRow == 4)
            sd.goToTheTextBox(T.ENTER_NEW_URL);
        else if(mainSelectedRow == 5)
            sd.goToTheTimingForm();         
    }
    public int getMainSelectedRow()
    {
        return mainSelectedRow;
    }
    public int getSelectedRow()
    {
        return selectedRow;
    }
}

