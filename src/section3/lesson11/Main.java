package section3.lesson11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static final String SOURCE_FILE = "./resources/lesson11/many-flowers.jpg";
    public static final String DESTINATION_FILE = "./out/many-flowers.jpg";
    public static void main(String[] args) throws IOException {
        BufferedImage originalImage = ImageIO.read(new File(SOURCE_FILE));
        BufferedImage resultImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        long startTime = System.currentTimeMillis();

        //recolorSingleThreaded(originalImage, resultImage);

        recolorMultiThreaded(originalImage, resultImage, 6);

        long endTime = System.currentTimeMillis();

        long duration = endTime - startTime;

        System.out.println(duration);

        File output = new File(DESTINATION_FILE);
        ImageIO.write(resultImage, "jpg", output);

    }

    public static void recolorSingleThreaded(BufferedImage originalImage, BufferedImage resultImage) {
        recolorImage(originalImage, resultImage, 0, 0,
                originalImage.getWidth(), originalImage.getHeight());
    }

    public static void recolorMultiThreaded(BufferedImage originalImage, BufferedImage resultImage, int numOfThreads) {
        List<Thread> threadList = new ArrayList<>();
        int width = originalImage.getWidth();
        int height = originalImage.getHeight() / numOfThreads;

        for (int i = 0; i < numOfThreads; i++) {
            final int THREAD_MULTIPLIER = i;

            Thread thread = new Thread(() -> {
                int leftCorner = 0;
                int topCorner = height * THREAD_MULTIPLIER;

                recolorImage(originalImage, resultImage, leftCorner, topCorner, width, height);
            });

            threadList.add(thread);
        }

        for (Thread thread : threadList) {
            thread.start();
        }

        for (Thread thread : threadList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void recolorImage(BufferedImage originalImage, BufferedImage resultImage,
                                    int leftCorner, int topCorner, int width, int height) {
        for (int x = leftCorner; x < leftCorner + width && x < originalImage.getWidth(); x++) {
            for (int y = topCorner; y < topCorner + height && y < originalImage.getHeight(); y++) {
                recolorPixel(originalImage, resultImage, x, y);
            }
        }
    }

    public static void recolorPixel(BufferedImage originalImage, BufferedImage resultImage, int x, int y) {
        int rgb = originalImage.getRGB(x, y);

        int r = getRed(rgb);
        int g = getGreen(rgb);
        int b = getBlue(rgb);

        int newR, newG, newB;
        if (isShadeOfGray(r, g, b)) {
            newR = Math.min(255, r + 10);
            newG = Math.max(0, g - 80);
            newB = Math.max(0, b - 20);
        } else {
            newR = r;
            newB = b;
            newG = g;
        }

        int newRGB = createRGBFromColors(newR, newG, newB);
        setRGB(resultImage, x, y, newRGB);
    }

    public static void setRGB(BufferedImage image, int x, int y, int rgb) {
        image.getRaster().setDataElements(x, y, image.getColorModel().getDataElements(rgb, null));
    }

    public static boolean isShadeOfGray(int r, int g, int b) {
        return Math.abs(r - g) < 30 && Math.abs(r - b) < 30 && Math.abs(g - b) < 30;
    }

    public static int createRGBFromColors(int r, int g, int b) {
        int rgb = 0;

        rgb |= r << 16;
        rgb |= g << 8;
        rgb |= b;

        rgb |= 0xFF000000;
        return rgb;
    }

    public static int getRed(int rgb) {
        return (rgb & 0x00FF0000) >> 16;
    }

    public static int getGreen(int rgb) {
        return (rgb & 0x0000FF00) >> 8;
    }

    public static int getBlue(int rgb) {
        return rgb & 0x000000FF;
    }
}
