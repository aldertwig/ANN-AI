package src;

/**
 * Node class is responsible for computing the training, testing and
 * validating a node as well as doing calculations on the images. The node is
 * assigned one label and a array of weights for each pixel index in all the
 * images.
 *
 * @author c14jpn, dv14jat
 * @date 2019-10-17
 */
public class Node
{
    private int label;              // The label to train on.
    private double[] weights;       // The weights for label.
    private double learningRate;    // The learning rate.

    /**
     * Constructor for Node.
     *
     * @param label int - Label the node will train for.
     * @param weightsSize int - Number of pixels for size of the weight array.
     */
    public Node(int label, int weightsSize, double learningRate)
    {
        this.label = label;
        this.learningRate = learningRate;
        this.weights = new double[weightsSize];
        // Initialize all weights to 0.
        for (int i = 0; i < weights.length; i++)
        {
            this.weights[i] = 0;
        }
    }

    /**
     * Trains the node on all the images in a array of ImageData
     * objects.
     *
     * @param imageSet ImageData - Array to train on.
     */
    public void train(ImageData[] imageSet)
    {
        for (int imageIndex = 0; imageIndex < imageSet.length; imageIndex++)
        {
            // Current nodes input, all pixels in current image.
            double[] nodeInput = imageSet[imageIndex].getPixels();

            // Compares expected output. -1 if wrong, 1 if right.
            double y = compareLabel(imageSet[imageIndex].getLabel(),
                                    this.label);

            // How large the error was in current nodes guess.
            double outputError = y - activationFunc(getDotProduct(nodeInput));

            for (int i = 0; i < nodeInput.length; i++)
            {
                // Calculate weight on current pixel.
                double weight = this.learningRate * outputError * nodeInput[i];

                // Update weight.
                this.weights[i] += weight;
            }
        }
    }

    /**
     * Tests a node on a ImageData array.
     *
     * @param imageSet ImageData[] - Array to test on.
     * @return double - How big the average error is in the node guess on
     * what label the image is.
     */
    public double test(ImageData[] imageSet)
    {
        // Track how wrong the guess is.
        double errorSum = 0;
        for (int imageIndex = 0; imageIndex < imageSet.length; imageIndex++)
        {
            // Current nodes input, all pixels in current image.
            double[] nodeInput = imageSet[imageIndex].getPixels();

            // Compares expected output. -1 if wrong, 1 if right.
            double y = compareLabel(imageSet[imageIndex].getLabel(),
                                    this.label);

            // How large the error was in current nodes guess.
            double outputError = y - activationFunc(getDotProduct(nodeInput));

            // Track error to know how much we need to train.
            errorSum += Math.abs(outputError);
        }
        return errorSum/imageSet.length;
    }

    /**
     * Returns a double corresponding to how likely it is that the input image
     * is the label assigned to the current network node.
     *
     * @param imagePixels double[] - Image to get output from.
     * @return double - Value how likely it is the image matches the node
     * label.
     */
    public double validate(double[] imagePixels)
    {
        return activationFunc(getDotProduct(imagePixels));
    }

    /**
     * Compares two labels.
     *
     * @param label int - First label to compare.
     * @param label2 int - Second label to compare.
     * @return double - 1 if the same, else -1.
     */
    private double compareLabel(int label, int label2)
    {
        return (label == label2) ? 1 : -1;
    }

    /**
     * Calculates the dot product between the pixel values and node weights.
     *
     * @param nodeInput double[] - Array of pixels
     * @return double - The sum of the products of all pixels and their weights.
     */
    private double getDotProduct(double[] nodeInput)
    {
        double sum = 0;
        for (int s = 0; s < this.weights.length; s++)
        {
            sum += (nodeInput[s] * this.weights[s]);
        }
        return sum;
    }

    /**
     * The activation function, returns value between -1 and 1 depending on
     * input. Should be used on the dot product to get a node nodes guess
     * on how likely the node label matches the image label. Uses tanh.
     *
     * @param x double - The input.
     * @return double - A value between -1 and 1 depending on x.
     */
    private double activationFunc(double x)
    {
        return Math.tanh(x);
    }

    public int getLabel()
    {
        return label;
    }
}
