import javax.microedition.lcdui.*;

public class TimingForm extends Canvas implements Runnable{

    private Command completeTiming;

    Thread t;
    int i = 0;

    TimingForm(Thread t, Command completeTiming)
    {
        this.t = t;
        this.completeTiming = completeTiming;
        Thread thisThread = new Thread(this);
        thisThread.start();
    }
    public void paint(Graphics g)
    {
        int width = g.getClipWidth();
        int height = g.getClipHeight();
        g.setColor(255,255,0);
        g.fillRect(0, 0, width, height);
        g.setColor(0,0,0);
        if(t.isAlive()) {
            g.drawString("Подождите, идет",10,10,0);
            g.drawString(" синхронизация " + Integer.toString(i),10,40,0);
        }
        else
            g.drawString("Синхронизация завершена",10,10,0);
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
