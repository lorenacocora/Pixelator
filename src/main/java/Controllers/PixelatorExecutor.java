package Controllers;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

public class PixelatorExecutor extends Thread {
    private final BufferedImage image;
    private final int totalSections;
    private int sectionWidth;
    private int sectionHeight;
    private int[][][] sections;

    public PixelatorExecutor(BufferedImage image, int sections) throws InterruptedException {
       this.image = image;
       this.totalSections = sections;
       pixelate();
    }

    public void pixelate() throws InterruptedException {
        sectionWidth = image.getWidth() / totalSections;
        sectionHeight = image.getHeight() / totalSections;
        sections = new int[totalSections][totalSections][3];
        int cores = Runtime.getRuntime().availableProcessors();
        PixelatorThread[] threads = new PixelatorThread[cores];

        // Create a thread for each core
        for (int i = 0; i < cores; i++) {
            threads[i] = new PixelatorThread(image, sectionWidth, sectionHeight, totalSections, sections);
            threads[i].start();
        }

        // Wait for all threads to finish
        for (PixelatorThread thread : threads) {
            thread.join();
        }
    }

    //Getters
    public int getTotalSections(){
        return totalSections;
    }

    public int getSectionWidth(){
        return sectionWidth;
    }

    public int getSectionHeight(){
        return sectionHeight;
    }

    public int[][][] getSections(){
        return sections;
    }
}
