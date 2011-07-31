import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;

abstract class myForm extends Canvas implements IMyForm
{
    protected Text text = new Text();
    protected Graphics g;

    protected int width;
    protected int height;
    protected int size;
    protected int mainIndent;
    protected int sideIndent;
    protected int fontHeight;
    protected int lowerIndent;

    protected int shift = - mainIndent;

    protected int mainSelectedRow = 1;
    protected int mainNumber;

    protected int selectedRow = 1;
    protected int number = 0;
    protected int indent = 0;
    protected String[] smallMenuList;

    private int selectedRowTopY = 0;
    private int selectedRowHeight = 0;

    protected boolean mainButtonIsPressed = false;

    protected myMultiLineText MLT;

    protected Display mainDisplay;

    public void paint(Graphics g)
    {
        mainNumber();
        this.g = g;
        MLT = new myMultiLineText(Font.SIZE_SMALL,Font.STYLE_BOLD,Font.FACE_PROPORTIONAL,g);
        width = g.getClipWidth();
        height = g.getClipHeight();
        lowerIndent = width/6;
        size = width/5;
        mainIndent = width/4;
        sideIndent = size;
        fontHeight = g.getFont().getHeight();
        g.setColor(255,255,255);
        g.fillRect(0, 0, width, height);
        drawSomething();
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
    protected void drawMenu()
    {
        getShift();
        String list[] = getList();
        String paths[] = getPaths();
        int topY = -shift;

        for(int i = 0; i < mainNumber; i++)
        {
            if(mainSelectedRow == i+1)
            {
                drawSelectedString(0, topY, width, size);
                indent = topY+size*3/4;
            }
            g.setColor(0,0,0);
            if(mainSelectedRow!=i && mainSelectedRow!=i+1)
                g.drawLine(0, topY, width, topY);
            drawImage(paths[i],size-8, size-8, 4, topY+4);
            String path = "/images/main/further.png";
            drawImage(path,size/2, size/2, width-size/4*3, topY+size/4);
            int numberOfLines = MLT.setText(list[i], width, size);
            int linesHeight = numberOfLines*fontHeight;
            MLT.drawMultStr(0, topY+(size-linesHeight)/2);
            topY = topY+size;
        }
        drawSign();
        g.setColor(0,0,0);
        if(mainSelectedRow!=mainNumber)
            g.drawLine(0, topY, width, topY);
        drawButtons();
    }
    protected void drawSmallMenu()
    {
        if(mainNumber!=0)
        {
            int topY = indent;
            int smallMenuHeight = 0;
            for(int i = 0; i < number; i++)
            {
                int numberOfLines = MLT.setText(smallMenuList[i], width*5/6,size);
                int allHeigh = numberOfLines*fontHeight+fontHeight;
                smallMenuHeight = smallMenuHeight+allHeigh;
            }
            g.setColor(255, 255, 255);
            g.fillRoundRect(width/6, topY, width*5/6, smallMenuHeight,size/3,size/3);
            g.setColor(0, 0, 0);
            g.drawRoundRect(width/6, topY, width*5/6, smallMenuHeight,size/3,size/3);
            for(int i = 0; i < number; i++)
            {
                int numberOfLines = MLT.setText(smallMenuList[i], width*5/6,size);
                int allHeigh = numberOfLines*fontHeight+fontHeight;
                g.setColor(50, 50, 50);
                if(selectedRow == i+1)
                    drawSelectedString(width/6, topY, width*5/6, allHeigh);
                MLT.drawMultStr(width/6, topY+fontHeight/2);
                topY = topY+allHeigh;
            }
        }
    }
    protected void drawList()
    {
        getShift();
        String list[] = getList();
        int topY = - shift;
        g.setColor(0,0,0);
        if(mainNumber == 0)
        {
            g.drawLine(0, topY, width, topY);
            MLT.setText("Словарь пуст", width, size);
            MLT.drawMultStr(0, topY+fontHeight/2);
        }
        else
        {
            for(int i = 0; i < mainNumber; i++)
            {
                g.setColor(0,0,0);
                if(mainSelectedRow!=i+1 && mainSelectedRow!=i+2)
                    g.drawLine(0, topY, width, topY);
                int numberOfLines = MLT.setText(list[i], width, size);
                int allHeigh = numberOfLines*fontHeight+fontHeight;
                if(mainSelectedRow == i+1)
                {
                    drawSelectedString(0, topY, width, allHeigh);
                    indent = topY+allHeigh-fontHeight/2;
                }
                MLT.drawMultStr(0, topY+fontHeight/2);
                topY = topY+allHeigh;
            }
        }
        drawSign();
    }
    protected void drawSelectedString(int x, int y, int width, int height)
    {

        int arc = height/3;

        for(int i = 0; i< height/2;i++)
        {
            int step = i*15/(height/2);
            int arcShift = 0;
            if(i<arc/3)
                arcShift = arc/3-i;
            g.setColor(200+step,200+step,200+step);

            g.drawLine(x+arcShift, y+i, x+width-arcShift, y+i);
            g.setColor(230-step,230-step,230-step);
            g.drawLine(x+arcShift, y+height-i-1, x+width-arcShift, y+height-i-1);
        }
        g.setColor(0,0,0);
        g.drawRoundRect(x, y, width, height, arc, arc);
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
    protected void drawSign()
    {
        g.setColor(255,255,255);
        g.fillRect(0, 0, width, mainIndent);
        g.setColor(0,0,0);
        drawImage(getMainPath(),mainIndent, mainIndent, 0, 0);
        int numberOfLines = MLT.setText(getMainName(), width-mainIndent-sideIndent,mainIndent);
        int linesHeight = numberOfLines*fontHeight;
        MLT.drawMultStr(mainIndent,(mainIndent-linesHeight)/2);
    }
    private void drawButtons()
    {

    }

    protected void getShift()
    {
        if(mainButtonIsPressed)
        {
            selectedRowTopY = getSelectedRowTopY();
            selectedRowHeight = getSelectedRowHeight();
        }
        else
        {
            selectedRowTopY = getMainSelectedRowTopY();
            selectedRowHeight = getMainSelectedRowHeight();
        }
        if(selectedRowTopY+selectedRowHeight+lowerIndent>height)
        {
                shift = shift+selectedRowTopY+selectedRowHeight+lowerIndent-height;
        }
        else if(selectedRowTopY<mainIndent)
        {
            shift = shift+selectedRowTopY-mainIndent;
        }
    }
    public int getMainSelectedRowTopY()
    {
        return (mainSelectedRow-1)*size-shift;
    }
    public int getMainSelectedRowHeight()
    {
        return  size;
    }
    public int getSelectedRowTopY()
    {
        int topY = getMainSelectedRowTopY()+ getMainSelectedRowHeight() - fontHeight;
        for(int i = 0; i < selectedRow-1;i++)
        {
            int numberOfLines = MLT.setText(smallMenuList[i], width*5/6, size);
            int allHeight = numberOfLines*fontHeight+fontHeight;
            topY = topY+allHeight;
        }
        return topY;
    }
    public int getSelectedRowHeight()
    {
        int numberOfLines = MLT.setText(smallMenuList[selectedRow-1], width*5/6, size);
        int allHeight = numberOfLines*fontHeight+fontHeight;
        return allHeight;
    }
}
