import javax.microedition.rms.*;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class Filter implements RecordFilter{
    
    private String search;
    private long lastTiming;
	private Boolean activeOnly;

    Filter(String search, Boolean activeOnly){
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
            if(search != null && !language.equals(search))
                return false;
            if(lastTiming != 1)
                if(lastTiming >= Long.parseLong(lastModified))
                    return false;
			
			if(activeOnly != null)
			{
				if(activeOnly.booleanValue() && "".equals(translation)
						|| !activeOnly.booleanValue() && !"".endsWith(translation))
				{		
					return false;
				}
			}
        }
        catch (IOException ioe) {}
		//System.out.prinln("Filter matching"); //TODO: (3.low) раскомментировать эту строчку, будет видно, что метод вызывается на каждый чих. Поэпытаться понять, почему так происходит и попробовать уменьшить количество вызовов до чего-то разумного.
        return true;
    }

}
