import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;

public abstract class myForm extends Canvas
{
    protected Text text = new Text();
    protected Graphics g;

    protected int width;
    private int height;
    protected int size;
    private int mainIndent;
    private int sideIndent;
    protected int fontHeight;
    private int lowerIndent;

    protected int shift;
    protected int mainSelectedRow = 1;
    protected int mainNumber;

    protected int selectedRow = 1;
    protected int number = 0;
    protected int indent = 0;
    protected String[] smallMenuList;

    private int selectedRowTopY = 0;
    private int selectedRowHeight = 0;

    private int lastPointerX = 0;
    private int lastPointerY = 0;
    private int firstPointerX = 0;
    private int firstPointerY = 0;
    
    private int minFormY;
    private int maxFormY;
    private int maxMainFormY;
    
    private boolean pressed = false;
    private boolean menuIsAll = true;   
    
    private boolean tu = false;
    private boolean td = true;   
    
    protected boolean mainButtonIsPressed = false;

    protected myMultiLineText MLT;

    public void paint(Graphics g)
    {
        initMainNumber();
        this.g = g;
        MLT = new myMultiLineText(Font.SIZE_SMALL,Font.STYLE_BOLD,Font.FACE_PROPORTIONAL,g);
        width = g.getClipWidth();
        height = g.getClipHeight();
        lowerIndent = 0;
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
    public void pointerPressed(int x, int y)
    {
        pressed = true;
                
        lastPointerX = x;
        lastPointerY = y;   
        
        firstPointerX = x;
        firstPointerY = y;   
        
        int minY, maxY;
        int k;
        if(y > mainIndent & y < maxMainFormY)
        {
            if(mainButtonIsPressed)
            {
                if(y > minFormY & y < maxFormY & x > width/6)
                {
                    k = getSelectedRowHeight();
                    minY = getSelectedRowTopY();
                    maxY =  minY + k;
                    while(y < minY || y > maxY)
                    {
                        k = getSelectedRowHeight();
                        minY = getSelectedRowTopY();
                        maxY =  minY + k;        
                        if(y < minY)
                            up();
                        else if(y > maxY)
                            down();
                        pressed = false;
                        if(selectedRow == number || selectedRow == 1)
                            break;
                    }  
                }
            }
            else if(mainNumber!=0) 
            {
                k = getMainSelectedRowHeight();
                minY = getMainSelectedRowTopY();
                maxY =  minY + k;
                while(y < minY || y > maxY)
                {
                    k = getMainSelectedRowHeight();
                    minY = getMainSelectedRowTopY();
                    maxY =  minY + k;        
                    if(y < minY)
                        up();
                    else if(y > maxY)
                        down();
                    pressed = false;
                    if(mainSelectedRow == mainNumber || mainSelectedRow == 1)
                        break;
                }  
            }     
        }
        else 
            pressed = false;
        repaint();
    }	
    protected void pointerReleased(int x, int y)
    {
        int maxShift = size/4;
        if(x < firstPointerX + maxShift 
        & x > firstPointerX - maxShift
        & y < firstPointerY + maxShift
        & y > firstPointerY - maxShift 
        & pressed)
            forward();
        repaint();
    }  
    protected void pointerDragged(int x, int y)
    {     
        
        int deltaX = x - lastPointerX;
        if(!menuIsAll)
        {        
            int deltaY = y - lastPointerY;  
            if(tu & deltaY > 0)
                   shift -=deltaY;
            if(td & deltaY < 0)
                   shift -=deltaY;  
            lastPointerY = y; 
        }
        if(pressed)
        {
            if(deltaX > size)
            {
                forward();
                pressed = false;
            }
            else if(deltaX < -size)
            {
                back();     
                pressed = false;
            }
        }           
        repaint();
    }
    abstract protected String[] getPaths();
    abstract protected String[] getList();
    abstract protected void up();
    abstract protected void down();
    abstract protected void back();
    abstract protected void forward();
    abstract protected String getMainPath();
    abstract protected String getMainName();
    abstract protected void initMainNumber();
    abstract protected void drawSomething();
	
    protected void drawMenu()
    {
        menuIsAll = true;
        tu = false;
        td = false;
        getShift();
        String list[] = getList();
        String paths[] = getPaths();
        int topY = -shift;

        for(int i = 0; i < mainNumber; i++)
        {
            int numberOfLines = MLT.setText(list[i], width-size*2, size);
            int linesHeight = numberOfLines*fontHeight;
            if(topY < mainIndent)
            {
                menuIsAll = false;
                tu = true;
                if(topY < 0)
                {
                    topY = topY+size;
                    continue; 
                }
            }
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
            MLT.drawMultStr(size, topY+(size-linesHeight)/2);
            topY = topY+size;
            if(topY > height)
            {
                menuIsAll = false;
                td = true;
                break;  
            }
        }
        maxMainFormY = topY;
        drawSign();
        g.setColor(0,0,0);
        if(mainSelectedRow!=mainNumber)
            g.drawLine(0, topY, width, topY);
    }

    protected void drawSmallMenu()
    {
        menuIsAll = true;
        tu = false;
        td = false;
        if(mainNumber!=0)
        {
            int topY = indent;
            minFormY = topY;
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
                if(topY < mainIndent)
                {
                    menuIsAll = false;
                    tu = true;
                    if(topY < 0)
                    {
                        topY = topY+allHeigh;
                        continue; 
                    }
                }
                g.setColor(50, 50, 50);
                if(selectedRow == i+1)
                    drawSelectedString(width/6, topY, width*5/6, allHeigh);
                MLT.drawMultStr(width/6, topY+fontHeight/2);
                topY = topY+allHeigh;
                if(topY > height)
                {
                    menuIsAll = false;
                    td = true;
                    break;  
                }
            }
            maxFormY = topY;
        }
    }
	
    protected void drawList()
    {
        menuIsAll = true;
        tu = false;
        td = false;
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
                int numberOfLines = MLT.setText(list[i], width, size);
                int allHeigh = numberOfLines*fontHeight+fontHeight;
                if(topY < mainIndent)
                {
                    menuIsAll = false;
                    tu = true;
                    if(topY < 0)
                    {
                        topY = topY+allHeigh;
                        continue; 
                    }
                }
                g.setColor(0,0,0);
                if(mainSelectedRow!=i && mainSelectedRow!=i+1)
                    g.drawLine(0, topY, width, topY);
                if(mainSelectedRow == i+1)
                {
                    drawSelectedString(0, topY, width, allHeigh);
                    indent = topY+allHeigh-fontHeight/2;
                }
                MLT.drawMultStr(0, topY+fontHeight/2);
                topY = topY+allHeigh;
                if(topY > height)
                {
                    menuIsAll = false;
                    td = true;
                    break;  
                }
            }
            maxMainFormY = topY;
            if(mainSelectedRow!=mainNumber)
                g.drawLine(0, topY, width, topY);
        }
        drawSign();
    }

    private void drawSelectedString(int x, int y, int width, int height)
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
        if(mainSelectedRow!=1)
            g.drawLine(0, mainIndent, width, mainIndent); 
    }
    private void getShift()
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
