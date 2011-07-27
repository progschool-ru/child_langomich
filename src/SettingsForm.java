import javax.microedition.lcdui.*;
import java.io.IOException;

public class SettingsForm extends Canvas implements CommandListener
{
    private Command change = new Command("Изменить", Command.SCREEN, 1);
    private Settings settings = new Settings();
    private Text text = new Text();

    private int width;
    private int height;
    private int size;

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
        width = g.getClipWidth();
        height = g.getClipHeight();
        size = width/5;
        g.setColor(0,0,0);
        g.fillRect(0, 0, width, height);
        drawMenu(g);
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
    private void drawMenu(Graphics g)
    {
        MLT = new MultiLineText(Font.SIZE_SMALL,Font.STYLE_BOLD,Font.FACE_PROPORTIONAL,g);
        if(selectedRow*size-shift>height)
        {
                shift = selectedRow*size-height;
        }
        else if((selectedRow-1)*size-shift<0)
        {
            shift = (selectedRow-1)*size;
        }
        for(int i = 1; i <= size; i++)
        {
            g.setColor(0,30+i,60+i*2);
            g.drawLine(0, (selectedRow-1)*size+i-shift, width, (selectedRow-1)*size+i-shift);
        }
        g.setColor(14,14,14);
        g.fillRect(size, 0, 3, number*size);
        String str[] = new String[7];
        str[0] = text.NUMBER_OF_WORDS+"  "+Integer.toString(settings.getNumberOfWords());
        str[1] = text.LANGUAGE+"  "+settings.getLanguage();
        str[2] = text.LOGIN+"  "+settings.getLanguage();
        str[3] = text.URL+"  "+settings.getURL();
        try
        {
            Image img = Image.createImage("word.png"); 
            g.drawImage(getImage(img,size-4, size-4), 2, 2-shift, 0);
            img = Image.createImage("lang.png");
            g.drawImage(getImage(img,size-4, size-4), 2, size+2-shift, 0);
            img = Image.createImage("login.png");
            g.drawImage(getImage(img,size-4, size-4), 2, size*2+2-shift, 0);
            img = Image.createImage("url.png");
            g.drawImage(getImage(img,size-4, size-4), 2, size*3+2-shift, 0);
        }
        catch(IOException ioe){}

        for(int i = 1; i <= number; i++)
        {
            g.setColor(14,14,14);
            g.fillRect(0, size*i-shift, width, 3);
            g.setColor(0, 0, 0);
            MLT.setText(size+size/10, size*(i-1)+size/10-shift, width-size-size/10,size*i-size/10, str[i-1]);
            MLT.DrawMultStr();
            g.setColor(120, 120, 120);
            MLT.setText(size+size/10+2, size*(i-1)+size/10+2-shift, width-size-size/10+2,size*i-size/10+2, str[i-1]);
            MLT.DrawMultStr();
        }
    }
    private Image getImage(Image image, int thumbWidth, int thumbHeight)
    {
        int x, y, pos, tmp, z = 0;
        final int sourceWidth = image.getWidth();
        final int sourceHeight = image.getHeight();
        final int ratio = sourceWidth / thumbWidth;
        final int[] in = new int[sourceWidth];
        final int[] out = new int[thumbWidth*thumbHeight];
        final int[] cols = new int[thumbWidth];
        for (x = 0,pos = 0; x < thumbWidth; x++)
        {
            cols[x] = pos;
            pos += ratio;
            tmp = pos + (thumbWidth - x) * ratio;
            if(tmp > sourceWidth)
            {
                pos--;
            }
            else if(tmp < sourceWidth - ratio)
            {
                pos++;
            }
        }
        for (y = 0, pos = 0, z = 0; y < thumbHeight; y++)
        {
            image.getRGB(in, 0, sourceWidth, 0, pos, sourceWidth, 1);
            for (x = 0; x < thumbWidth; x++, z++)
            {
                out[z] = in[cols[x]];
            }
            pos += ratio;
            tmp = pos + (thumbHeight - y) * ratio;
            if(tmp > sourceHeight)
            {
                pos--;
            }
            else if(tmp < sourceHeight - ratio)
            {
                pos++;
            }
        }
        return Image.createRGBImage(out, thumbWidth, thumbHeight, true);
    }
}

