import javax.microedition.rms.*;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class Filter implements RecordFilter{
    
    private String search;
    private int number;
    
    Filter(String search, int number){
        this.search = search;
        this.number = number;
    }
    public boolean matches(byte[] candidate){
        ByteArrayInputStream bais = new ByteArrayInputStream(candidate);
        DataInputStream dis = new DataInputStream(bais);
        String name = "";
        try
        {
            for(int i = 0; i < number;i++)
                name = dis.readUTF();
            if(name.equals(search))
                return true;
        }
        catch (IOException ioe) {}
        return false;
    }

}
