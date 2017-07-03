package main.localiser.misztal;

import pl.edu.uj.JImageStream.collectors.BufferedImageCollector;
import pl.edu.uj.JImageStream.filters.noise.GaussFilter;
import pl.edu.uj.JImageStream.model.StreamableImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws IOException {
        BufferedImage sourceImage = ImageIO.read(new File("./src\\main\\resources\\CASIA-Iris-Thousand\\CASIA-Iris-Thousand\\000\\L\\S5000L00.jpg"));
        StreamableImage streamableImage = new StreamableImage(sourceImage);

        BufferedImage bufferedImage = streamableImage.stream()
                .apply(new GaussFilter(15, 1))
                .collect(new BufferedImageCollector());

        Instant start = Instant.now();

        BufferedImage bufferedImageResult = streamableImage.stream()
                .apply(new GaussFilter(15, 1))
                .apply(new OtsuBinarization(true))
                .apply(new Erosion(5))
                .apply(new Dilation(5))
                .collect(new BufferedImageCollector());

        //BufferedImage bufferedImageResult = streamableImage.stream().collect(new BufferedImageCollector());

        Instant end = Instant.now();
        System.out.println(Duration.between(start, end).toMillis());

        new ImageFrame(bufferedImageResult);

        Point center = getMassCentre(bufferedImageResult);
        System.out.println(center);
        //drawCircle(bufferedImage, center, 20);
        //drawCircle(bufferedImage, center, 50);

        drawCircle(bufferedImage, new Point(140, 145), 1);
        drawCircle(bufferedImage, new Point(140, 148), 39);
        new ImageFrame(bufferedImage);

        System.out.println(Arrays.toString(fdo(bufferedImage, center)));
    }

    /**
     * funckcja pomocnicza ograniczająca obszar początkowego przeszukiwania
     * zajdujemy środek masy (x0,y0)
     * i potem szukamy w x0-10;x0+10 oraz y0-10;y0+10
     * @param image
     * @return
     */
    public static Point getMassCentre(BufferedImage image) {
        final int width = image.getWidth();
        final int height = image.getHeight();

        double st = width * height;
        double x = 0;
        double y = 0;
        int n = 0;
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                if (image.getRGB(i, j) == Color.black.getRGB()) {
                    x += i;
                    y += j;
                    ++n;
                }
            }
        }

        x /= n;
        y /= n;

        return new Point((int) x, (int) y);
    }

    /**
     * rysowanie okręgu na obrazku
     * @param image
     * @param where
     * @param radius
     */
    public static void drawCircle(BufferedImage image, Point where, int radius) {
        Graphics2D g = image.createGraphics();
        g.setColor(Color.white);
        g.drawOval(where.x - radius, where.y - radius, 2 * radius, 2 * radius);
    }

    /**
     * funkcja wyznacza wartość całki kołowej po okręgu o środku w p0 oraz promieniu r
     * @param image
     * @param p0
     * @param r
     * @return
     */
    public static double f(BufferedImage image, Point p0, int r) {
        double ret = 0;
        final int k = 18;
        for (double s = 0; s < 2. * Math.PI; s += Math.PI / k) {
            /*ADDED*/
            int x = (int) (p0.x + Math.cos(s) * r);
            int y = (int) (p0.y + Math.sin(s) * r);
            x = x<0 ? 0 : x;
            y = y<0 ? 0 : y;
            /*ADDED*/

            ret += red(image.getRGB(x, y)) * Math.PI / k * r;

        }
        ret /= (2 * Math.PI * r);

        return ret;
    }

    /**
     * funkcja wynacza promień dla którego wartość pochodnej jest maxymalna dla środka w p0
     * @param image
     * @param p0
     * @return
     */
    public static Object[] fd(BufferedImage image, Point p0) {
        int size = (int) (0.8 * Math.max(image.getHeight(), image.getWidth()) / 2) - 10;
        double[] vals = new double[size];

//        double r0 = f(image, p0, 10);
//        int r = 11;
//        double v = Double.MIN_VALUE;
//        double r1 = 0;
//        double tmp;
        for (int i = 0; i < size; i++) {
            vals[i] = f(image, p0, i + 10);
        }

        double v = 0;
        double v_max = Double.MIN_VALUE;
        int r = 0;
        for (int i = 0; i < size - 1; i++) {
            v = Math.abs(vals[i] - vals[i + 1]);
            if (v > v_max) {
                v_max = v;
                r = i + 10;
            }
        }

        return new Object[]{Integer.valueOf(r), Double.valueOf(v)};
    }

    /**
     * pełny operator rózniczkowo-całkowy (oczywiście wcześniej mamy w tym gaussa
     * @param image
     * @param p0
     * @return
     */
    public static Object[] fdo(BufferedImage image, Point p0) {
        double v = Double.MIN_VALUE;
        int r = 0;
        Point p = null;
        //TODO what is this supposed to be?
        for (int i = -10; i < 10; ++i) {
            for (int j = -10; j < 10; ++j) {
                Point point = new Point(p0.x + i, p0.y + j);
                Object[] d = fd(image, point);
                if ((Double) d[1] > v) {
                    v = (Double) d[1];
                    r = (Integer) d[0];
                    p = point;
                }
//                drawCircle(image, point, 15);
            }

            break;
        }
        return new Object[]{p, r};
    }

    public static int red(int val) {
//        return (val & 0x00FF0000) >>> 16;
        return (val >> 16) & 0x000000FF;
    }
}
