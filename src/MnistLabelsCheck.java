/**
 * Johan Ahlqvist
 */
package src;

import java.io.File;
import java.util.Scanner;

public class MnistLabelsCheck
{
    Scanner resultFile;
    Scanner validationFile;   

    public static void main(String[] args)
    {
        if (args.length != 2) {
            System.out.println("Usage: java MnistLabelsCheck <result file> validation-labels.txt");
            System.exit(1);
        }

        MnistLabelsCheck mnistCheck = new MnistLabelsCheck(args[0], args[1]);
    }
      
    public MnistLabelsCheck(String resultFileName, String validationFileName)
    {
        try {
            resultFile = new Scanner(new File(resultFileName));
            validationFile = new Scanner(new File(validationFileName));
        } catch (Exception ex) {
            System.err.println("Exception: " + ex.getMessage());
            System.exit(1);
        }

        // Skip two lines of file information
        validationFile.nextLine();
        validationFile.nextLine();
        // Get the number of labels
        int nlabels = validationFile.nextInt();
        // Go to next line where labels start
        validationFile.nextLine();                 

        int correct = 0;
        // Check each label with result label
        for (int i = 0; i < nlabels; i++) {
            int vlabel = validationFile.nextInt();
            int rlabel = resultFile.nextInt();
            if (vlabel == rlabel) {
                correct +=  1;
            }
        }

        System.out.println("Percentage of correct classifications: " + 
                           (100.0 * correct) / nlabels + "% out of " + 
                           nlabels + " images");
    }  
}
