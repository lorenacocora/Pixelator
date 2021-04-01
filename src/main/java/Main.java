import Controllers.PixelatorExecutor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        BufferedImage result = null;
        try {
            result = ImageIO.read(new File("image.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            new PixelatorExecutor(result, 10).pixelate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        MainFX.main(args);
    }
}
