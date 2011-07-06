import javax.microedition.lcdui.*;

public class TimingForm extends Canvas implements Runnable{

    private Command completeTiming;
    private Text text = new Text();
    private MultiLineText MLT;
    Thread t;
    int i = 0;

    TimingForm(Timing t, Command completeTiming)
    {
        this.t = t;
        this.completeTiming = completeTiming;
        Thread thisThread = new Thread(this);
        thisThread.start();
        MLT = new MultiLineText();

    }
    public void paint(Graphics g)
    {
        int width = g.getClipWidth();
        int height = g.getClipHeight();
        g.setColor(255,255,255);
        g.fillRect(0, 0, width, height);
        if(t.isAlive()) 
        {
            g.setColor(200,200,255);
            g.fillRect(width/10, 70, width/10*7-5, 20);
            g.setColor(0,0,0);
            g.drawString(text.PLEASE_WAIT,10,10,0);
            g.drawString(text.TIMING,10,40,0);
            g.setColor(100,100,255);
            g.fillRect((i%7+1)*width/10, 70, 20, 20);
        }
        else
        {
            g.setColor(0,0,0);
            g.drawString(text.TIMING_COMPLETE,10,10,0);
            MLT.SetTextPar(30, 30, width-60,height-60,5,Font.SIZE_LARGE,Font.STYLE_BOLD,Font.FACE_PROPORTIONAL,g, new Settings().getText());
            MLT.DrawMultStr();
        }
    }
    public void run()
    {
        while(t.isAlive())
        {
            i++;
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
