import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class MainForm extends myForm implements CommandListener
{
    private Text text = new Text();
    Display mainDisplay;
    private int width;
    private int height;
    private int size;
    private int mainIndent;
    private int sideIndent;
    private int fontHeight;
	private Graphics g;
	private myMultiLineText MLT;
    private boolean mainButtonIsPressed = false;
    private int mainSelectedRow = 1;
    private int shift = 0;
    private int mainNumber = 5;
    private int indent = 0;
    private int selectedRow = 0;
    private int number = 0;
    private String[] smallMenuList;

    public void paint(Graphics g)
    {
        this.g = g;
        MLT = new myMultiLineText(Font.SIZE_SMALL,Font.STYLE_BOLD,Font.FACE_PROPORTIONAL,g);
        width = g.getClipWidth();
        height = g.getClipHeight();
        size = width/5;
        mainIndent = width/4;
        sideIndent = size;
        fontHeight = g.getFont().getHeight();
        g.setColor(255,255,255);
        g.fillRect(0, 0, width, height);
        drawMenu();
        if(mainButtonIsPressed)
            drawSmallMenu();
    }
    private void drawMenu()
    {      
        getShift();
        drawSelectedString(0, (mainSelectedRow-1)*size-shift+mainIndent, width);
        String paths[] = getPaths();
        String list[] = getList();
        for(int i = 0; i < mainNumber; i++)
        {
            if(mainSelectedRow!=i+1 && mainSelectedRow!=i+2)
                g.drawLine(0, size*(i+1)+mainIndent-shift, width, size*(i+1)+mainIndent-shift);
            drawImage(paths[i],size-8, size-8, 4, size*i+6-shift+mainIndent);
            String path = "/images/main/further.png";
            drawImage(path,size/2, size/2, width-size/4*3, size*i+size/4-shift+mainIndent);
            
            g.setColor(225, 225, 225);
            int numberOfLines = MLT.setText(list[i], width-size-size/10-sideIndent,size*(i+1)-size/10);
            int linesHeight = numberOfLines*fontHeight;
            MLT.drawMultStr(size+size/8-1, size*i+(size-linesHeight)/2-shift+mainIndent-1);
            g.setColor(0, 0, 0);
            MLT.setText(list[i], width-size-size/10-sideIndent,size*(i+1)-size/10);
            MLT.drawMultStr(size+size/8, size*i+(size-linesHeight)/2-shift+mainIndent);
        }
        g.setColor(255,255,255);
        g.fillRect(0, 0, width, mainIndent);
        g.setColor(0,0,0);
        drawImage("/images/main/settings.png",mainIndent, mainIndent, 0, 0);
        int numberOfLines = MLT.setText(text.SETTINGS, width-mainIndent-sideIndent,mainIndent);
        int linesHeight = numberOfLines*fontHeight;
        MLT.drawMultStr(mainIndent,(mainIndent-linesHeight)/2);
        if(mainSelectedRow!=1)
            g.drawLine(0, mainIndent, width, mainIndent);
    }
    private void drawSmallMenu()
    {
        g.setColor(255, 255, 255);
        g.fillRect(width/6, mainIndent+indent-shift, width*5/6, size*number);
        g.setColor(0, 0, 0);
        g.drawRect(width/6, mainIndent+indent-shift, width*5/6, size*number);
        drawSelectedString(width/6, (selectedRow-1)*size+mainIndent+indent-shift, width*5/6);

        for(int i = 0; i < number; i++)
        {
            g.setColor(0,0,0);
            if(selectedRow!=i+1 && selectedRow!=i+2)
                g.drawLine(width/6, size*(i+1)+mainIndent+indent-shift, width, size*(i+1)+mainIndent+indent-shift);
            g.setColor(225, 225, 225);
            int numberOfLines = MLT.setText(smallMenuList[i], width*5/6-size/10,size*(i+1)-size/10);
            int linesHeight = numberOfLines*fontHeight;
            MLT.drawMultStr(width/6+size/8-1, size*i+(size-linesHeight)/2-shift+mainIndent+indent-1);

            g.setColor(0, 0, 0);
            MLT.setText(smallMenuList[i], width*5/6-size/10,size*(i+1)-size/10);
            MLT.drawMultStr(width/6+size/8, size*i+(size-linesHeight)/2-shift+mainIndent+indent);
        }
    }
    private void getShift()
    {
        if(mainButtonIsPressed)
        {
            if(selectedRow*size-shift+mainIndent+indent>height)
            {
                    shift = selectedRow*size-height+mainIndent+indent;
            }
            else if((selectedRow-1)*size-shift+mainIndent+indent<0)
            {
                shift = (selectedRow-1)*size+mainIndent+indent;
            }  
        }
        else
        {
            if(mainSelectedRow*size-shift>height)
            {
                    shift = mainSelectedRow*size-height;
            }
            else if((mainSelectedRow-1)*size-shift<0)
            {
                shift = (mainSelectedRow-1)*size;
            }
        }
    }
    private void drawSelectedString(int x, int y, int width)
    {
        int arc = size/3;

        for(int i = 0; i< size/2;i++)
        {
            int step = i*15/(size/2);
            int arcShift = 0;
            if(i<arc/3)
                arcShift = arc/3-i;
            g.setColor(200+step,200+step,200+step);
            
            g.drawLine(x+arcShift, y+i, x+width-arcShift, y+i);
            g.setColor(230-step,230-step,230-step);
            g.drawLine(x+arcShift, y+size-i-1, x+width-arcShift, y+size-i-1);
        }
        g.setColor(0,0,0);
        g.drawRoundRect(x, y, width, size, arc, arc);
    }
    private boolean drawImage(String path, int newWidth, int newHeight, int x, int y)
    {     
        try
        {
            myImage myImage  = new myImage();
            Image img = Image.createImage(path);
            img = myImage.scale(img, newWidth, newHeight);
            g.drawImage(img, x, y, 0);  
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }
	
	
	
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
    public void up()
    {
        if(mainSelectedRow!=1)
            mainSelectedRow--;
        else
            mainSelectedRow = mainNumber;
        repaint();
    }
    public void down()
    {
        if(mainSelectedRow != mainNumber)
            mainSelectedRow++;
        else
            mainSelectedRow=1;
        repaint();
    }
    public void forward()
    {
        dieIsCast();
        repaint();
    }
    public void back()
    {

    }
    public void mainNumber()
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
    public String[] getList()
    {
        String list[] = new String[mainNumber];
        list[0] = text.START;
        list[1] = text.ADD_WORD;
        list[2] = text.DICTIONARY;
        list[3] = text.SETTINGS;
        return list;
    }
    public String[] getPaths()
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
//            ListForm lf = new ListForm(mainDisplay, this);
//            mainDisplay.setCurrent(lf);
        }
        else if(mainSelectedRow == 4)
        {
            SettingsForm sf = new SettingsForm(mainDisplay, sd.mainList);
            mainDisplay.setCurrent(sf);
        }
    }
}
