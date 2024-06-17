package src;

/**
 * Class that contains the data of an image.
 *
 * @author c14jpn, dv14jat
 * @date 2019-10-17
 */
public class ImageData
{
    private double[] pixels;    // Array of all the pixels in the image.
    private int label;          // Correct label for the image.

    /**
     * Constructor for ImageData.
     *
     * @param pixels Double[] - Array with pixels.
     */
    public ImageData(double[] pixels)
    {
        this.pixels = pixels;
    }

    public double[] getPixels()
    {
        return pixels;
    }

    public int getLabel()
    {
        return label;
    }

    public void setLabel(int label)
    {
        this.label = label;
    }
}
