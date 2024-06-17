package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;

 /**
  *  Main class with the responsibility for parsing files taken from arguments
  *  in main, as well as creating the artificial neural network.
  *
  * @author c14jpn, dv14jat
  * @date 2019-10-17
 */
public class Digits
{
     private static int imagePixelSize;     // Amount of pixels in each image.
     private static int[] labels;           // Labels to train for.
     private static ANNetwork anNetwork;    // The artificial neural network.

     /**
      * Main function
      *
      * @param args String - String array containing filenames to read.
      */
     public static void main(String[] args)
     {
         // Check if correct arguments is given.
         if (args.length != 3) {
             System.out.println("Usage: java Digits training-images.txt " +
                     "training-labels.txt validation-images.txt");
             System.exit(1);
         }

         // File with images to train on.
         String imageTrainFile = args[0];
         // File with image labels to train on.
         String imageTrainLabelFile = args[1];
         // File with images to validate on.
         String imageValidationFile = args[2];

         // Get images to train on.
         ImageData[] trainingImages = getImages(imageTrainFile);
         // Get images to validate on.
         ImageData[] validationImages = getImages(imageValidationFile);

         // If training images, validation images and labels is present,
         // create the artificial neural network and then train and validate
         // networks.
         if ((trainingImages != null) && (validationImages != null) &&
                 getLabels(imageTrainLabelFile, trainingImages))
         {
             // Create the artificial neural network.
             anNetwork = new ANNetwork(labels, imagePixelSize);
             // Create the node network.
             anNetwork.createNodeNetwork();
             // Train the node network.
             anNetwork.trainNodeNetwork(trainingImages);
             // Validate the node network.
             anNetwork.validateNodeNetwork(validationImages);
         }
     }

     /**
      * Reads the label files and saves each label into the corresponding
      * ImageData object.
      *
      * @param fileName String - Name of file to open.
      * @param imageSet ImageData[] - set of images to store labels.
      * @return boolean - True if successful, false if not.
      */
     private static boolean getLabels(String fileName, ImageData[] imageSet)
     {
         try {
             BufferedReader br = new BufferedReader(new FileReader(fileName));
             String strLine;
             int iCount = 0; // To track current ImageData object index.
             boolean skippedInfoLine = false; // If information line is skipped.

             // Read until end of file.
             while ((strLine = br.readLine()) != null)
             {
                 // Skip comment lines.
                 if (!strLine.startsWith("#"))
                 {
                     if (skippedInfoLine)
                     {
                         // Set label on current ImageData object.
                         int imageLabel = Integer.parseInt(strLine);
                         imageSet[iCount].setLabel(imageLabel);
                         iCount++;
                     } else {
                         // Skip first line with information about file content.
                         skippedInfoLine = true;
                     }
                 }
             }
             br.close();
         } catch (Exception e) {
             System.err.println("Error: " + e.getMessage());
             System.exit(1);
         }
         return true;
     }

     /**
      * Reads the image file and saves each image into it's own ImageData
      * object, containing a array of its pixels.
      *
      * @param fileName String - Name of the image file to read
      * @return ImageData[] - Array of ImageData objects on success, or NULL if
      * failed
      */
     private static ImageData[] getImages(String fileName)
     {
         ImageData[] imageSet = null;
         try {
             BufferedReader br = new BufferedReader(new FileReader(fileName));
             String strLine;
             boolean imageInfo = false;
             int iCount = 0;
             // Read until end of file.
             while ((strLine = br.readLine()) != null)
             {
                 // Skip comment line.
                 if (!strLine.startsWith("#"))
                 {
                     // First line containing information about content of file.
                     if (!imageInfo)
                     {
                         String imagesInfo[] = strLine.split(" ");

                         // Get number of images.
                         int imageCount = Integer.parseInt(imagesInfo[0]);
                         // Get number of Rows in each image.
                         int imageRows = Integer.parseInt(imagesInfo[1]);
                         // Get number of Columns in each image.
                         int imageCols = Integer.parseInt(imagesInfo[2]);

                         // Split the string of labels in to separate labels.
                         String strLabels[] = imagesInfo[3].split("");
                         // Store labels in int array.
                         labels = new int[strLabels.length];
                         for (int i = 0; i < strLabels.length; i++)
                         {
                             labels[i] = Integer.parseInt(strLabels[i]);
                         }
                         // Calculate number of pixels in each image.
                         imagePixelSize = imageRows * imageCols;
                         // Create image array set for all images.
                         imageSet = new ImageData[imageCount];
                         imageInfo = true; // Info about images saved.
                     } else {
                         // Parse rest of file containing data on each image.

                         // Split each pixel on each image into a string array.
                         String[] strImagePixels = strLine.split(" ");

                         // Cast to double and save in array.
                         double[] imagePixels = Arrays.stream(strImagePixels).
                                 mapToDouble(Double::parseDouble).
                                 toArray();

                         // Divide each pixel value by 1000 for ease of use.
                         for (int i = 0; i < imagePixels.length; i++)
                         {
                             imagePixels[i] /= 1000.0;
                         }

                         // Save into ImageData object.
                         imageSet[iCount] = new ImageData(imagePixels);
                         iCount++;
                     }
                 }
             }
             br.close();
         } catch (Exception e) {
             System.err.println("Error: " + e.getMessage());
             System.exit(1);
         }
         return imageSet;
     }
 }
