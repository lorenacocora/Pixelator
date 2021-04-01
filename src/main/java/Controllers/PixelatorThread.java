package Controllers;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class PixelatorThread extends Thread {
    static Semaphore sectionSemaphore = new Semaphore(1);
    static Semaphore nextSectionSemaphore = new Semaphore(1);
    static int lastX = -1;
    static int lastY = 0;
    private int totalSections;
    private BufferedImage image;
    private int sectionWidth;
    private int sectionHeight;
    private int[][][] sections;

    public PixelatorThread(BufferedImage image, int sectionWidth, int sectionHeight, int totalSections, int[][][] sections) {
        this.image = image;
        this.sectionWidth = sectionWidth;
        this.sectionHeight = sectionHeight;
        this.totalSections = totalSections;
        this.sections = sections;
    }

    public void run() {
        int currentX = -1;
        int currentY = 0;
        int totalPixels = sectionWidth * sectionHeight;
        while(true) {
            try {
                if(nextSectionSemaphore.tryAcquire(10, TimeUnit.SECONDS)) {
                    if (lastX >= totalSections - 1 && lastY >= totalSections - 1) {
                        break;
                    } else if (lastX >= totalSections - 1) {
                        lastX = 0;
                        lastY += 1;
                    } else {
                        lastX++;
                    }

                    currentX = lastX;
                    currentY = lastY;
                }  else {
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                nextSectionSemaphore.release();
            }

            // Calculate the section offset
            int x = currentX * sectionWidth;
            int y = currentY * sectionHeight;

            // Get the section data
            Rectangle section = new Rectangle(x, y, sectionWidth, sectionHeight);
            Raster data = image.getData(section);

            int[] pixelRGB = new int[3];
            int[] averageRGB = new int[3];

            // Go through all the pixels in the section
            for (int pixelX = x; pixelX < x + sectionWidth; pixelX++) {
                for (int pixelY = y; pixelY < y + sectionHeight; pixelY++) {
                    data.getPixel(pixelX, pixelY, pixelRGB);

                    // Add to the average of Red
                    averageRGB[0] += pixelRGB[0];
                    // Add to the average of Blue
                    averageRGB[1] += pixelRGB[1];
                    // Add to the average of Green
                    averageRGB[2] += pixelRGB[2];
                }
            }
            // Calculate the average of Red
            averageRGB[0] /= totalPixels;
            // Calculate the average of Green
            averageRGB[1] /= totalPixels;
            // Calculate the average of Blue
            averageRGB[2] /= totalPixels;

            try {
                if(sectionSemaphore.tryAcquire(10, TimeUnit.SECONDS)) {
                    sections[currentY][currentX] = averageRGB;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                sectionSemaphore.release();
            }
        }
    }
}
