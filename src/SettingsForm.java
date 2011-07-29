import javax.microedition.lcdui.*;

public class SettingsForm extends Canvas implements CommandListener
{
    private Text text = new Text();
    private Command choice = new Command(text.CHOICE, Command.SCREEN, 1);
    private Command back = new Command(text.BACK, Command.EXIT, 0);
    private Settings settings = new Settings();
    private Graphics g;
    
    private int width;
    private int height;
    private int size;
    private int mainIndent;
    private int sideIndent;
    private int fontHeight;

    private int shift = 0;

    private int mainSelectedRow = 1;
    private int mainNumber = 4;
    
    private int selectedRow = 0;
    private int number = 0;
    private int indent = 0;
    private String[] smallMenuList;
    
    private boolean mainButtonIsPressed = false;

    private myMultiLineText MLT;

    Display mainDisplay;
    List mainList;

    SettingsForm(Display mainDisplay, List mainList)
    {
        this.mainList = mainList;
        this.mainDisplay = mainDisplay;
        this.addCommand(back);
        this.addCommand(choice);
        this.setCommandListener(this);
    }
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
    public void commandAction(Command c, Displayable s)
    {
        if(c == back)
            back();
        if(c == choice)
            forward();
    }
    public void keyPressed(int keyCode)
    {
        int act = getGameAction(keyCode);
        if(act==Canvas.UP)
            up();
        else if(act == Canvas.DOWN)
            down();
        else if(act == Canvas.RIGHT || act == Canvas.FIRE)
            forward();
        else if(act == Canvas.LEFT)
            back();
    }
    private void up()
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
    private void down()
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
    private void forward()
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
    private void back()
    {
        if(mainButtonIsPressed)
            mainButtonIsPressed = false;
        else
            mainDisplay.setCurrent(mainList);
        repaint();
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
            img = myImage.getImage(img, newWidth, newHeight);
            g.drawImage(img, x, y, 0);  
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }
    private String[] getList()
    {
        String list[] = new String[mainNumber];
        list[0] = text.NUMBER_OF_WORDS+"  "+Integer.toString(settings.getNumberOfWords());
        list[1] = text.LANGUAGE+"  "+settings.getLanguage();
        list[2] = text.LOGIN+"  "+settings.getLanguage();
        list[3] = text.URL+"  "+settings.getURL();
        return list;
    }
    private String[] getPaths()
    {
        String paths[] = new String[mainNumber];
        String path = "/images/settings";
        paths[0] = path+"/word.png";
        paths[1] = path+"/lang.png";
        paths[2] = path+"/login.png";
        paths[3] = path+"/url.png";
        return paths;
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
        else
        {
            String str[]= new String[1];
            str[0] = "Ввести новый адрес";
            smallMenuList = str;
        }
        number = smallMenuList.length; 
    }
    private void dieIsCast()
    {
        if(mainSelectedRow == 1)
        {
            settings.setNumberOfWords(Integer.parseInt(smallMenuList[selectedRow-1]));
        }
        else if(mainSelectedRow == 2)
        {
            if(selectedRow == number)
            {
                
            }
            else
                settings.setLanguage(smallMenuList[selectedRow-1]);
        }
        else if(mainSelectedRow == 3)
        {

        }
        else
        {

        }
    }
}

