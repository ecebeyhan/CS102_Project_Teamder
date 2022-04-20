package classes;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;

public class ImageHandler {


    public static void editImage(ActionEvent event, File imageFile, ImageView imageView, boolean imgChanged) throws IOException {
        // get the stage to open a new window to select an image
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        // initialize the file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an image");

        // set the extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files (*.jpg, *.png)", "*.jpg", "*.png");
        FileChooser.ExtensionFilter extFilter2 = new FileChooser.ExtensionFilter("All files (*.*)", "*.*");
        fileChooser.getExtensionFilters().addAll(extFilter, extFilter2);

        // set the initial directory
        String userDirectoryString = System.getProperty("user.home");  // it works for windows as far as I know
        File userDirectory = new File(userDirectoryString);
        fileChooser.setInitialDirectory(userDirectory);

        // open the file dialog
        File tmpImageFile = fileChooser.showOpenDialog(stage);

        if (tmpImageFile != null) {
            imageFile = tmpImageFile;

            // update the image view
            if (imageFile.isFile()) {
                try {
                    BufferedImage bufferedImage = ImageIO.read(imageFile);
                    Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                    imageView.setImage(image);
                    imgChanged = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * Copy image to specified directory and
     * give a new unique name generated by getImageFileName() method.
     */
    public static void copyImageFile(File imageFile) throws IOException {

        //create a new Path to copy the image into a local directory
        Path sourcePath = imageFile.toPath();

        String uniqueFileName = getUniqueFileName(imageFile.getName());

        Path targetPath = Paths.get("./src/main/java/images/"+uniqueFileName);

        //copy the file to the new directory
        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

        //update the imageFile to point to the new File
        imageFile = new File(targetPath.toString());
    }

    /**
     * Generate a unique file name for the image file.
     * Guarantees that the file name is unique.
     * @param oldFileName the old file name to be changed
     * @return a unique file name
     */
    private static String getUniqueFileName(String oldFileName){
        String newName;

        //create a Random Number Generator
        SecureRandom rng = new SecureRandom();

        //loop until we have a unique file name
        do
        {
            newName = "";

            //generate 32 random characters
            for (int count=1; count <=32; count++)
            {
                int nextChar;

                do
                {
                    nextChar = rng.nextInt(123);
                } while(!validCharacterValue(nextChar));

                newName = String.format("%s%c", newName, nextChar);
            }
            newName += oldFileName;

        } while (!uniqueFileInDirectory(newName));

        return newName;
    }

    /**
     * Check if the file name is unique in the directory.
     * @param fileName the file name to check
     * @return true if the file name is unique, false otherwise
     */
    public static boolean uniqueFileInDirectory(String fileName){
        File directory = new File("./src/main/java/images/");

        File[] dir_contents = directory.listFiles();

        for (File file: dir_contents)
        {
            if (file.getName().equals(fileName))
                return false;
        }
        return true;
    }

    /**
     * Check if the character is a valid character.
     * Given corresponds to the ASCII table.
     * ASCII is a set of characters that can be represented by a single byte.
     * @param asciiValue
     * @return if the character is a valid character
     */
    public static boolean validCharacterValue(int asciiValue) {
        //0-9 = ASCII range 48 to 57
        if (asciiValue >= 48 && asciiValue <= 57)
            return true;

        //A-Z = ASCII range 65 to 90
        if (asciiValue >= 65 && asciiValue <= 90)
            return true;

        //a-z = ASCII range 97 to 122
        if (asciiValue >= 97 && asciiValue <= 122)
            return true;

        return false;
    }
}
