package ThreadExcutioner;
import Controllers.PixelatorExecutor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// ParBegin
//      Execute Bruno's threads wait for them to finish
// ParEnd
//      Execute new thread to treat data received from Bruno's threads
public class Executor {
    private BufferedImage image;
    private int sections;


    public Executor(BufferedImage image, int sections) throws Exception
    {
        this.image = image;
        this.sections = sections;

        //Instantiation of threads (objects)
        //This classes / objects are just an example.
        PixelatorExecutor pixExec = new PixelatorExecutor(image, sections);

        //Thread Start (starts executing threads)
        pixExec.start();

        //Threads Joins (join wait for thread to die)
        pixExec.join();

        //Create and execute last Thread for merging into a final image
        mergeImg imageMerged = new mergeImg(image, pixExec.getSectionWidth(), pixExec.getSectionHeight(), pixExec.getTotalSections(), pixExec.getSections());
        imageMerged.start();
        imageMerged.join();

        System.out.println("All threads has been executed!");
    }

    private static class mergeImg extends Thread {

        private BufferedImage image;
        private int sectionWidth;
        private int sectionHeight;
        private int totalSections;
        private int[][][] sections;

        public mergeImg(BufferedImage image, int sectionWidth, int sectionHeight, int totalSections, int[][][] sections){

            this.image = image;
            this.sectionWidth = sectionWidth;
            this.sectionHeight = sectionHeight;
            this.totalSections = totalSections;
            this.sections = sections;
        }

        public void run(){
            // Create the new image to show the result
            BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), ColorSpace.TYPE_RGB);

            // Go through the pixels and set the color the same as the corresponding section
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    int sectionX = Math.min(x / sectionWidth, totalSections - 1);
                    int sectionY = Math.min(y / sectionHeight, totalSections - 1);
                    Color color = new Color(sections[sectionY][sectionX][0], sections[sectionY][sectionX][1], sections[sectionY][sectionX][2]);
                    newImage.setRGB(x, y, color.getRGB());
                }
            }
            File outputFile = new File("image-out.jpg");
            try {
                ImageIO.write(newImage, "jpg", outputFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
