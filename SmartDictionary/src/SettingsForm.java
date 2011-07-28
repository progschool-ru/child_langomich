import javax.microedition.lcdui.*;

public class SettingsForm extends Canvas implements CommandListener
{
    private Text text = new Text();
    private Command change = new Command(text.CHANGE, Command.SCREEN, 1);
    private Settings settings = new Settings();
    private Graphics g;
    
    private int width;
    private int height;
    private int size;
    private int mainIndent;
    private int sideIndent;

    private int shift = 0;

    private int selectedRow = 1;
    private int number = 4;

    private MultiLineText MLT;

    SettingsForm(Command back)
    {
        this.addCommand(back);
        this.addCommand(change);
    }
    public void paint(Graphics g)
    {
        this.g = g;
        width = g.getClipWidth();
        height = g.getClipHeight();
        size = width/5;
        mainIndent = width/4;
        sideIndent = size;
        g.setColor(255,255,255);
        g.fillRect(0, 0, width, height);
        drawMenu();
    }
    public void commandAction(Command c, Displayable s)
    {

    }
    public void keyPressed(int keyCode)
    {
        int act = getGameAction(keyCode);
        if(act==Canvas.UP)
            Up();
        else if(act == Canvas.DOWN)
            Down();
        repaint();
    }
    private void Up()
    {
        if(selectedRow!=1)
            selectedRow--;
        else
            selectedRow=number;
    }
    private void Down()
    {
        if(selectedRow != number)
            selectedRow++;
        else
            selectedRow=1;
    }
    private void drawMenu()
    {     
        getShift();
        g.setColor(0,0,0);
        drawImage("/images/main/settings.png",mainIndent, mainIndent, 0, 0);
        MLT = new MultiLineText(Font.SIZE_SMALL,Font.STYLE_BOLD,Font.FACE_PROPORTIONAL,g);
        MLT.setText(mainIndent,0, width-mainIndent-sideIndent,mainIndent, "Настройки");
        MLT.DrawMultStr();
        if(selectedRow!=1)
            g.drawLine(0, mainIndent, width, mainIndent);
        drawSelectedString((selectedRow-1)*size-shift+mainIndent);

        String paths[] = getPaths();
        String list[] = getList();
        for(int i = 0; i < number; i++)
        {
            if(selectedRow!=i+1 && selectedRow!=i+2)
                g.drawLine(0, size*(i+1)+mainIndent, width, size*(i+1)+mainIndent);
            drawImage(paths[i],size-8, size-8, 4, size*i+6-shift+mainIndent);
            drawImage("/images/main/further.png",size/2, size/2, width-size/4*3, size*i+size/4-shift+mainIndent);
            MLT = new MultiLineText(Font.SIZE_SMALL,Font.STYLE_BOLD,Font.FACE_PROPORTIONAL,g);

            g.setColor(225, 225, 225);
            MLT.setText(size+size/8-1, size*i+size/10-shift+mainIndent-1, width-size-size/10-sideIndent,size*(i+1)-size/10, list[i]);
            MLT.DrawMultStr();
            g.setColor(0, 0, 0);
            MLT.setText(size+size/8, size*i+size/10-shift+mainIndent, width-size-size/10-sideIndent,size*(i+1)-size/10, list[i]);
            MLT.DrawMultStr();   
        }
    }
    private void drawSelectedString(int y)
    {
        int arc = size/3;

        for(int i = 0; i< size/2;i++)
        {
            int step = i*15/(size/2);
            int arcShift = 0;
            if(i<arc/3)
                arcShift = arc/3-i;
            g.setColor(200+step,200+step,200+step);
            
            g.drawLine(arcShift, y+i, width-arcShift, y+i);
            g.setColor(230-step,230-step,230-step);
            g.drawLine(arcShift, y+size-i-1, width-arcShift, y+size-i-1);
        }

        g.setColor(0,0,0);
        g.drawRoundRect(0, y, width, size, arc, arc);

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
        String list[] = new String[number];
        list[0] = text.NUMBER_OF_WORDS+"  "+Integer.toString(settings.getNumberOfWords());
        list[1] = text.LANGUAGE+"  "+settings.getLanguage();
        list[2] = text.LOGIN+"  "+settings.getLanguage();
        list[3] = text.URL+"  "+settings.getURL();
        return list;
    }
    private String[] getPaths()
    {
        String paths[] = new String[number];
        String path = "/images/settings";
        paths[0] = path+"/word.png";
        paths[1] = path+"/lang.png";
        paths[2] = path+"/login.png";
        paths[3] = path+"/url.png";
        return paths;
    }
    private void getShift()
    {
        if(selectedRow*size-shift+mainIndent>height)
        {
                shift = selectedRow*size-height+mainIndent;
        }
        else if((selectedRow-1)*size-shift+mainIndent<0)
        {
            shift = (selectedRow-1)*size+mainIndent;
        }
    }
}

