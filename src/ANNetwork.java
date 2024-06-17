package src;

import java.util.Arrays;
import java.util.Random;

/**
 * Class that represents a network which contains all the nodes.
 *
 * @author c14jpn, dv14jat
 * @date 2019-10-17
 */
public class ANNetwork
{
    private static Node[] nodes;        // Array with each node.
    private static int imagePixelSize;  // Amount of pixels in each image.
    private static int[] labels;        // Labels to train for.
    private static final double LEARNING_RATE = 0.1;   // The learning rate.

    /**
     * Constructor for the artificial neural network. Takes a label array
     * with a label for each node.
     *
     * @param labels int[] - The labels to create nodes for.
     * @param imagePixelSize int - The amount of pixels in each image.
     */
    public ANNetwork(int[] labels, int imagePixelSize)
    {
        this.labels = labels;
        this.imagePixelSize = imagePixelSize;
    }

    /**
     * Creates all the nodes needed, one for each label.
     */
    public static void createNodeNetwork()
    {
        nodes = new Node[labels.length];
        for (int i = 0; i < labels.length; i++)
        {
            nodes[i] = new Node(labels[i], imagePixelSize, LEARNING_RATE);
        }
    }

    /**
     * Calls the function to shuffle all ImageData objects and calls the
     * train function in the Node class on each node object.
     *
     * @param trainingImages ImageData[] - set of images to train on.
     */
    public static void trainNodeNetwork(ImageData[] trainingImages)
    {
        double errorThreshold = 0.1; // The error threshold to train towards.
        // Train each node.
        for (int i = 0; i < nodes.length; i++)
        {
            // Tracks how big node error is.
            double averageError;
            do {
                ImageData[] trainingImagesSet = shuffleImages(trainingImages);
                // How big set the node train on.
                int trainSetSize = trainingImages.length/3 * 2;
                // How big set the node test on.
                int testSetSize = trainingImages.length - trainSetSize;

                // ImageData array of our train images.
                ImageData[] trainSetImages = Arrays.copyOfRange(
                                             trainingImagesSet, 0,
                                             trainSetSize);
                // ImageData array of our test images.
                ImageData[] testSetImages = Arrays.copyOfRange(
                                            trainingImagesSet,
                                            (trainingImagesSet.length -
                                            testSetSize),
                                            trainingImagesSet.length);

                // Train one node.
                nodes[i].train(trainSetImages);
                // Test one node.
                averageError = nodes[i].test(testSetImages);
                // Continue train node until error is below threshold.
            } while (averageError > errorThreshold);
        }
    }

    /**
     * Run the trained nodes on a set of validation images and print the
     * classified label to standard output. Calls the validate function in the
     * Node class.
     *
     * @param validationImages ImageData[] - Array of images to validate on.
     */
    public static void validateNodeNetwork(ImageData[] validationImages)
    {
        // Label the node image classification represent.
        int label = 0;
        for (int i = 0; i < validationImages.length; i++)
        {
            // To track previous outputs.
            double output = 0;
            for (int j = 0; j < nodes.length; j++)
            {
                // Get output from node validation.
                double networkOutput = nodes[j].validate(
                        validationImages[i].getPixels());
                // If the new output is higher than previous output or its
                // first cycle.
                if ((output <= networkOutput) || (j == 0))
                {
                    label = nodes[j].getLabel();
                    output = networkOutput;
                }
            }
            System.out.println(label);
        }
    }

    /**
     * Shuffles all the ImageData objects in its array to get a random spread.
     *
     * @param images ImageData[] - Array to shuffle.
     * @return ImageData[] - The shuffled array.
     */
    private static ImageData[] shuffleImages(ImageData[] images)
    {
        int shuffleIndex;           // Current array index.
        ImageData tempImage;        // Current image.
        Random rand = new Random(); // Random number generator.

        for (int imageIndex = 0; imageIndex < images.length; imageIndex++)
        {
            // Temporary save current image.
            tempImage = images[imageIndex];
            // Get random index.
            shuffleIndex = rand.nextInt(images.length);
            // Move random ImageData object to current index.
            images[imageIndex] = images[shuffleIndex];
            // Move the temporary saved image to that random position.
            images[shuffleIndex] = tempImage;
        }
        return images;
    }
}
