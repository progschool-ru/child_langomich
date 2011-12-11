package org.smdme;

import javax.microedition.rms.*;
import java.io.*;

class Ordering implements RecordComparator
{
    int column;
    Ordering(int column) {
      this.column = column;
    }
    public int compare(byte [] reel, byte [] rec2)
    {
        ByteArrayInputStream baisl = new ByteArrayInputStream(reel);
        DataInputStream disl = new DataInputStream(baisl);
        ByteArrayInputStream bais2 = new ByteArrayInputStream(rec2);
        DataInputStream dis2 = new DataInputStream(bais2);
        String namel = null;
        String name2 = null;
        try
        {
            namel = disl.readUTF ();
            name2 = dis2.readUTF () ;
            if(column > 1)
            {
                namel = disl.readUTF ();
                name2 = dis2.readUTF () ;
            }
        }
        catch (IOException ioe) {}
        if(namel == null || name2 == null)
            return 0;
        int result = namel.compareTo(name2);
        if (result < 0)
            return RecordComparator.PRECEDES;
        else if (result == 0)
            return RecordComparator.EQUIVALENT;
        else
            return RecordComparator.FOLLOWS;
    }
}
