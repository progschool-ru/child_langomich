import javax.microedition.rms.*;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class Filter implements RecordFilter{
    
    private String search;
    private long lastTiming;

    Filter(String search){
        this.search = search;
        lastTiming = 1;
    }
    Filter(String search, long lastTiming){
        this.search = search;
        this.lastTiming = lastTiming;
    }
    public boolean matches(byte[] candidate){
        ByteArrayInputStream bais = new ByteArrayInputStream(candidate);
        DataInputStream dis = new DataInputStream(bais);
        String language = "";
        String lastModified = "";
        try
        {
            for(int i = 0; i < 4;i++)
                language = dis.readUTF();
                lastModified = dis.readUTF();
            if(!language.equals(search))
                return false;
            if(lastTiming != 1)
                if(lastTiming > Long.parseLong(lastModified))
                    return false;
        }
        catch (IOException ioe) {}
        return true;
    }

}
