import javax.microedition.lcdui.Image;

public class myImage {
    public Image getImage(Image image, int thumbWidth, int thumbHeight)
    {
        int x, y, pos, tmp, z = 0;
        final int sourceWidth = image.getWidth();
        final int sourceHeight = image.getHeight();
        final int ratio = sourceWidth / thumbWidth;
        final int[] in = new int[sourceWidth];
        final int[] out = new int[thumbWidth*thumbHeight];
        final int[] cols = new int[thumbWidth];
        for (x = 0,pos = 0; x < thumbWidth; x++)
        {
            cols[x] = pos;
            pos += ratio;
            tmp = pos + (thumbWidth - x) * ratio;
            if(tmp > sourceWidth)
            {
                pos--;
            }
            else if(tmp < sourceWidth - ratio)
            {
                pos++;
            }
        }
        for (y = 0, pos = 0, z = 0; y < thumbHeight; y++)
        {
            image.getRGB(in, 0, sourceWidth, 0, pos, sourceWidth, 1);
            for (x = 0; x < thumbWidth; x++, z++)
            {
                out[z] = in[cols[x]];
            }
            pos += ratio;
            tmp = pos + (thumbHeight - y) * ratio;
            if(tmp > sourceHeight)
            {
                pos--;
            }
            else if(tmp < sourceHeight - ratio)
            {
                pos++;
            }
        }
        return Image.createRGBImage(out, thumbWidth, thumbHeight, true);
    }
}
