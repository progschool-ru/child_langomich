import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.TextField;

public class SettingsForm extends myForm implements CommandListener
{
    private Command choice = new Command(text.CHOICE, Command.SCREEN, 1);
    private Command back = new Command(text.BACK, Command.EXIT, 0);
    private Command save = new Command(text.SAVE, Command.SCREEN, 1);
    private Command cancel = new Command(text.CANCEL, Command.EXIT, 0);
    private Command completeTiming = new Command(text.NEXT, Command.SCREEN, 1);

    private Settings settings = new Settings();

    TextBox newText;

    Canvas mainForm;

    SettingsForm(Display mainDisplay, Canvas mainForm)
    {
        this.mainForm = mainForm;
        this.mainDisplay = mainDisplay;
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
        if (c ==  cancel)
            mainDisplay.setCurrent(this);
        if (c ==  save)
        {
            if(mainSelectedRow == 2)
            {
                settings.setLanguage(newText.getString());
                new Languages().newLanguage(settings.getLanguage());
            }
            else if(mainSelectedRow == 3)
            {
                if(selectedRow == 1)
                    settings.setLogin(newText.getString());
                else
                    settings.setPassword(newText.getString());
            }
            else
                settings.setURL(newText.getString());
            mainDisplay.setCurrent(this);
        }
       if (c == completeTiming )
       {
            settings = new Settings();
            mainDisplay.setCurrent(this);
       }
    }

    public void up()
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
    public void down()
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
    public void forward()
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
    public void back()
    {
        if(mainButtonIsPressed)
            mainButtonIsPressed = false;
        else
            mainDisplay.setCurrent(mainForm);
        repaint();
    }
    public void mainNumber()
    {
        mainNumber = 5;
    }
    public void drawSomething()
    {
        drawMenu();
        if(mainButtonIsPressed)
            drawSmallMenu();
    }
    public String[] getList()
    {
        String list[] = new String[mainNumber];
        list[0] = text.NUMBER_OF_WORDS+"  "+Integer.toString(settings.getNumberOfWords());
        list[1] = text.LANGUAGE+"  "+settings.getLanguage();
        list[2] = text.LOGIN+"  "+settings.getLogin();
        list[3] = text.URL+"  "+settings.getURL();
        list[4] = text.TIMING;
        return list;
    }
    public String[] getPaths()
    {
        String paths[] = new String[mainNumber];
        String path = "/images/settings";
        paths[0] = path+"/word.png";
        paths[1] = path+"/lang.png";
        paths[2] = path+"/login.png";
        paths[3] = path+"/url.png";
        paths[4] = path+"/timing.png";
        return paths;
    }
    public String getMainPath()
    {
        return "/images/main/settings.png";
    }
    public String getMainName()
    {
        return text.SETTINGS;
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

            String str[] = new Languages().getLanguages();
            if(str != null)
            {
                smallMenuList = new String[str.length+1];
                for(i = 0; i < str.length; i++)
                    smallMenuList[i] = str[i];
            }
            else
                smallMenuList = new String[1];
            smallMenuList[i]= "Новый язык";
        }
        else if(mainSelectedRow == 3)
        {
            smallMenuList = new String[2];
            smallMenuList[0]="Ввести логин";
            smallMenuList[1]="Ввести пароль";
        }
        else if(mainSelectedRow == 5)
        {
            String str[]= new String[1];
            str[0] = "Пуск";
            smallMenuList = str;
        }
        else
        {
            String str[]= new String[1];
            str[0] = "Ввести новый адрес";
            smallMenuList = str;
        }
        number = smallMenuList.length; 
    }
    public void dieIsCast()
    {
        if(mainSelectedRow == 1)
        {
            settings.setNumberOfWords(Integer.parseInt(smallMenuList[selectedRow-1]));
        }
        else if(mainSelectedRow == 2)
        {
            if(selectedRow == number)
                newText(text.NEW_LANGUAGE);
            else
                settings.setLanguage(smallMenuList[selectedRow-1]);
        }
        else if(mainSelectedRow == 3)
        {
            if(selectedRow == 1)
                newText("Введите новый логин");
            else
                newText("Введите новый пароль");
        }
        else if(mainSelectedRow == 5)
        {
            try
            {
                 TimingForm timingForm = new TimingForm(new Timing(), completeTiming);
                 timingForm.setCommandListener(this);
                 mainDisplay.setCurrent(timingForm);
            }
            catch(Exception e){}
        }
        else
            newText("Введите новый адрес");
    }
    private void newText(String title)
    {
        newText = new TextBox(title, "", 20, TextField.ANY);
        newText.addCommand(cancel);
        newText.addCommand(save);
        newText.setCommandListener(this);
        mainDisplay.setCurrent(newText);
    }
}

