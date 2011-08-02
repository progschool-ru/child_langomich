import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Canvas;

public class ListForm extends myForm implements CommandListener
{
    private Command choice = new Command(text.CHOICE, Command.SCREEN, 1);
    private Command back = new Command(text.BACK, Command.EXIT, 0);

    Dictionary dictionary = new Dictionary();

    Canvas mainForm;

    ListForm(Display mainDisplay, Canvas mainForm)
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
    }
    public void up()
    {
        if(mainButtonIsPressed )
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
        if(mainButtonIsPressed )
        {
            dieIsCast();
            mainButtonIsPressed = false;
        }
        else if(mainNumber!=0)
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
        mainNumber = dictionary.getNumRecords();
    }
    public void drawSomething()
    {
        drawList();
        if(mainButtonIsPressed)
            drawSmallMenu();
    }
    public String[] getList()
    {
        String list[] = dictionary.getFullRecords();
        return list;
    }
    public String[] getPaths()
    {
        String paths[] = new String[mainNumber];
        return paths;
    }
    public String getMainPath()
    {
        return "/images/main/dictionary.png";
    }
    public String getMainName()
    {
        return text.DICTIONARY;
    }
    public void dieIsCast()
    {
        if(selectedRow == 1)
        {
            dictionary.deleteRecord(mainSelectedRow);
            if(mainSelectedRow == mainNumber)
               mainSelectedRow--;
            mainNumber--;
        }
        else if(selectedRow == 2)
        {
            dictionary.newOrdering(1);
        }
        else if(selectedRow == 3)
        {
            dictionary.newOrdering(2);
        }
        repaint();
    }
    private void smallMenuInit()
    {
        selectedRow = 1;
        String str[] = new String[3];
        str[0]="Удалить";
        str[1]="Сортировать по первой записи";
        str[2]="Сортировать по второй записи";
        smallMenuList = str;
        number = smallMenuList.length;
    }
    public int getMainSelectedRowTopY()
    {
        if(mainNumber!= 0)
        {
            String list[] = getList();
            int topY = - shift;
            for(int i = 0; i < mainSelectedRow-1;i++)
            {
                int numberOfLines = MLT.setText(list[i], width*5/6, size);
                int allHeight = numberOfLines*fontHeight+fontHeight;
                topY = topY+allHeight;
            }
            return topY;
        }
        return - shift;
    }
    public int getMainSelectedRowHeight()
    {
        if(mainNumber!= 0)
        {
            String list[] = getList();
            int numberOfLines = MLT.setText(list[mainSelectedRow-1], width*5/6, size);
            int allHeight = numberOfLines*fontHeight+fontHeight;
            return allHeight;
        }
        return 0;
    }
}
