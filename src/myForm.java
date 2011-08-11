import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public abstract class myForm extends Canvas // (trunk) implements IMyForm
{
    protected Text text = new Text();
	protected Graphics g;

    protected int width;
    private int height;
    protected int size;
    private int mainIndent;
    private int sideIndent;
    protected int fontHeight;
	//(trunk) private int lowerIndent;

	protected int shift = 0; //(trunk) = -mainIndent;

	protected int mainSelectedRow = 1;
    protected int mainNumber = 5; //(trunk) mainNumber;

	protected int selectedRow = 0; //(trunk) = 1;
    protected int number = 0;
    protected int indent = 0;
    protected String[] smallMenuList;

	//(trunk) private int selectedRowTopY = 0;
	//(trunk) private int selectedRowHeight = 0;

	protected boolean mainButtonIsPressed = false;

	protected myMultiLineText MLT;

	Display mainDisplay;

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

	public void keyPressed(int keyCode) // (trunk) moved to parent
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
	
	abstract protected String[] getPaths(); // (trunk) there are no these methods in trunk.
	abstract protected String[] getList();
	abstract protected void up();
	abstract protected void down();
	abstract protected void back();
	abstract protected void forward();
	abstract protected String getMainPath();
	abstract protected String getMainName();
	
    protected void drawMenu()
    {      
        getShift();
        drawSelectedString(0, (mainSelectedRow-1)*size-shift+mainIndent, width, size);
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

	protected void drawSmallMenu()
    {
        g.setColor(255, 255, 255);
        g.fillRect(width/6, mainIndent+indent-shift, width*5/6, size*number);
        g.setColor(0, 0, 0);
        g.drawRect(width/6, mainIndent+indent-shift, width*5/6, size*number);
        drawSelectedString(width/6, (selectedRow-1)*size+mainIndent+indent-shift, width*5/6, size);

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
    }
	
	// (trunk) private void drawButtons(){} // That's strange
	
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
	
	// (trunk) public int getMainSelectedRowTopY();
	// (trunk) public int getMainSelectedRowHeight();
	// (trunk) public int getSelectedRowTopY();
	// (trunk) public int getSelectedRowHeight();
}
