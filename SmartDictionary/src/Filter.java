import javax.microedition.rms.*;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class Filter implements RecordFilter{
    
    private String search;
    private long lastTiming;
	private boolean activeOnly;

    Filter(String search, boolean activeOnly){
        this.search = search;
        lastTiming = 1;
		this.activeOnly = activeOnly;
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
		String translation = "";
        try
        {
			int i = 0;
			for(; i < Dictionary.TRANSLATION; i++)
			{
				translation = dis.readUTF();
			}
            for(; i < Dictionary.LANGUAGE; i++)
			{
                language = dis.readUTF();
			}
            lastModified = dis.readUTF();
            if(!language.equals(search))
                return false;
            if(lastTiming != 1)
                if(lastTiming >= Long.parseLong(lastModified))
                    return false;
			
			if(activeOnly && "".equals(translation))
				return false;
        }
        catch (IOException ioe) {}
        return true;
    }

}
