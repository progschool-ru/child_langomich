package org.smdme;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Font;

public class myMultiLineText
{
    private int w,h,fsz,fst,fty;
    private int hStr;
    private int y0;
    private int dy;
    private int textheight;
    private Vector StringLines;
    private Graphics g;
    private int gx,gy,gw,gh;

    private String str1;

    myMultiLineText
    (
        int FontSize,
        int FontStyle,
        int FontType,
        Graphics graph
    )
    {
        this.fsz=FontSize;
        this.fst=FontStyle;
        this.fty=FontType;
        this.g=graph;
    }
    public int setText
    (
        String LongString,
        int width,
        int height
    )
    {
        this.w=width;
        this.h=height;
        gx=g.getClipX();
        gy=g.getClipY();
        gw=g.getClipWidth();
        gh=g.getClipHeight();
        g.setFont(Font.getFont(fty, fst, fsz));
        StringLines=null;
        StringLines =new Vector(1);
        int i0=0,i=0,in=0,j,jw=0;
        int imax=LongString.length();
        hStr=g.getFont().getHeight();
        boolean isexit=true;
        y0=0;
        while (isexit)
        {
            i=LongString.indexOf(" ", i0+1);
            if (i<=i0)
            {
                i=imax;
                isexit=false;
            }
            j=g.getFont().stringWidth(LongString.substring(i0,i));
            if (jw+j<w)
            {
                jw=jw+j;
                i0=i;
            } else
            {
                StringLines.addElement(LongString.substring(in,i0));
                in=i0+1;
                jw=j;
                if (j>w)
                {
                    i=i0;
                  while (jw>w)
                  {
                    j=0;
                    while (j<w)
                    {
                        i=i+1;
                        j=g.getFont().stringWidth(LongString.substring(in,i));

                    }
                    i=i-1;
                    j=g.getFont().stringWidth(LongString.substring(in,i));
                    StringLines.addElement(LongString.substring(in,i));
                    jw=jw-j;
                    i0=i;
                    in=i;
                  }
                  jw=0;
                }else{i0=i;}
            }
        }
        StringLines.addElement(LongString.substring(in,imax));
        textheight=StringLines.size()*hStr;
        return StringLines.size();
    }

    public void drawMultStr(int x, int y)
    {
       int y1;
       g.setClip(x, y, w, h);
       y1=y0;
       g.setFont(Font.getFont(fty, fst, fsz));
       for (int i=0;i<StringLines.size();i++)
       {
           int indent = (w - g.getFont().stringWidth(StringLines.elementAt(i).toString()))/2;
           if ((y1+hStr)>0){
           g.drawString(StringLines.elementAt(i).toString(), x+indent, y+y1, g.LEFT|g.TOP);
           }
           y1=y1+hStr;
           if (y1>h){break;}
       }
       g.setClip(gx, gy, gw, gh);
    }

}
