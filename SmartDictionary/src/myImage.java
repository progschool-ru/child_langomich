import javax.microedition.lcdui.Image;

public class myImage {
    public Image scale(Image image, int w, int h)
    {
        int w0 = image.getWidth();
        int h0 = image.getHeight();
        int[] arrayOld = new int[w0*h0];
        int[] arrayNew = new int[w*h];
        image.getRGB(arrayOld, 0, w0, 0, 0, w0, h0);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                 arrayNew[x+w*y] = arrayOld[x*w0/w+w0*(int)(y*h0/h)];
            }
        }
        return Image.createRGBImage(arrayNew, w, h, true);
    }
}
