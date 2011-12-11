package org.smdme;

import javax.microedition.lcdui.*;

public class TimingForm extends Canvas implements Runnable{

    private Command completeTiming;
    private Text text = new Text();
    private myMultiLineText MLT;
    private Image mobile,server;
    Timing t;
    int i = 0,k = 0;  

    TimingForm(Timing t, Command completeTiming)
    {
        this.t = t;
        this.completeTiming = completeTiming;
    }
    public void start()
    {
        new Thread(this).start();
        t.start();
    }
    public void paint(Graphics g)
    {
        MLT = new myMultiLineText(Font.SIZE_SMALL,Font.STYLE_BOLD,Font.FACE_PROPORTIONAL,g);
        int width = g.getClipWidth();
        int height = g.getClipHeight();
        int size  = width/6;
        g.setColor(255,255,255);
        g.fillRect(0, 0, width, height);
        myImage myImage  = new myImage();
        try
        {
            mobile = Image.createImage("/org/images/timing/mobile.png");
            mobile = myImage.scale(mobile, size, size);
            server = Image.createImage("/org/images/timing/server.png");
            server = myImage.scale(server, size, size);
        }
        catch(Exception e){}
        if(t.isAlive()) 
        {
            g.setColor(0,0,0);
            MLT.setText(text.PLEASE_WAIT, width-size,size);
            MLT.drawMultStr(size/2, size);
            MLT.setText(t.getText(), width-size,size);
            MLT.drawMultStr(size/2, size*7/2);
            
            g.setColor(20,20,255);
            int step = (i+1);
            if (step > 0 && step<=7)
                g.fillRect(size*7/4+step*size/4, size*5/2-size/6, size/6, size/6);
            g.setColor(100,100,255);
            if(k < 2)
                step = i;
            else 
                step = i + 2;
            if (step > 0 && step<=7)
                g.fillRect(size*7/4+step*size/4, size*5/2-size/6, size/6, size/6);
            g.setColor(160,160,255);
            if(k < 2)
                step = i - 1;
            else 
                step = i + 3;
            if (step > 0 && step<=7)
                g.fillRect(size*7/4+step*size/4, size*5/2-size/6, size/6, size/6);
            
            g.drawImage(mobile, size, size*2, 0);
            g.drawImage(server, width-size*2, size*2, 0);
        }
        else
        {
            g.setColor(0,0,0);
            MLT.setText(t.getText(), width-size,size);
            MLT.drawMultStr(size/2, size);
        }
    }
    public void run()
    {
        while(t.isAlive())
        {
            if(k == 0 || k == 1)
            {
                i++;
                if(i > 7)
                    k++;
            }
            else if(k == 2 || k == 3)
            {
                i--;
                if(i < 0)
                    k++; 
            }
            else
                k = 0;
            repaint();
            try
            {
                Thread.sleep(100);
            }
            catch(Exception e){}
        }
        repaint();
	this.addCommand(completeTiming);
    }
}
