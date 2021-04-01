package Controllers;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

public class PixelatorExecutor {
    private final BufferedImage image;
    private final int totalSections;

    public PixelatorExecutor(BufferedImage image, int sections) {
       this.image = image;
       this.totalSections = sections;
    }

    public void pixelate() throws InterruptedException, IOException {
        int sectionWidth = image.getWidth() / totalSections;
        int sectionHeight = image.getHeight() / totalSections;
        int[][][] sections = new int[totalSections][totalSections][3];
        int cores = Runtime.getRuntime().availableProcessors();
        PixelatorThread[] threads = new PixelatorThread[cores];

        // Create a thread for each core
        for (int i = 0; i < cores; i++) {
            threads[i] = new PixelatorThread(image, sectionWidth, sectionHeight, totalSections, sections);
            threads[i].start();
        }

        // Wait for all threads to finish
        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }

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
        ImageIO.write(newImage, "jpg", outputFile);
    }
}
