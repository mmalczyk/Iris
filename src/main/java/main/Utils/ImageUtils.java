package main.Utils;

import org.bytedeco.javacpp.opencv_core;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import static org.bytedeco.javacpp.opencv_core.cvCreateImage;

/**
 * Created by Magda on 29/05/2017.
 */
public class ImageUtils {
    //static class

    public static void showImage(String name, Mat src){
        BufferedImage image = toBufferedImage(src);
        showBufferedImage(image, name);
    }

    public static void showImage(String name, Mat src, int resize){
        if (resize>1)
            src = resizeImage(src, resize);
        showImage(name, src);
    }


    public static BufferedImage toBufferedImage(Mat m){
        //https://stackoverflow.com/questions/15670933/opencv-java-load-image-to-gui
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if ( m.channels() > 1 ) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }

        //TODO check bufferSize
        //https://stackoverflow.com/questions/26441072/finding-the-size-in-bytes-of-cvmat
        //int bufferSize = m.channels()*m.cols()*m.rows()*Double.BYTES;
        long bufferSize = m.step1(0) * m.rows();
        //TODO I really don't like this conversion
        byte [] b = new byte[(int)bufferSize];
        m.get(0,0, b); // get all the pixels
        BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return image;

    }

    public static void showBufferedImage(Mat img, String name){
        showBufferedImage(matToBufferedImage(img), name);
    }

    public static void showBufferedImage(BufferedImage img, String name){
        JFrame frame = new JFrame(name);
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(new JLabel(new ImageIcon(img)));
        frame.pack();
        frame.setVisible(true);
    }

    public static Mat bufferedImageToMat(BufferedImage image){
        //http://enfanote.blogspot.com/2013/06/converting-java-bufferedimage-to-opencv.html

        byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        Mat mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
        mat.put(0, 0, data);

        return  mat;
    }

    public static BufferedImage matToBufferedImage(Mat m){
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if ( m.channels() > 1 ) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = m.channels()*m.cols()*m.rows();
        byte [] b = new byte[bufferSize];
        m.get(0,0,b); // get all the pixels
        BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return image;

    }

    public static opencv_core.IplImage blankIplImageCopy (opencv_core.IplImage src){
        return cvCreateImage(src.cvSize(), src.depth(), src.nChannels());
    }


    public static Mat resizeImage (Mat src, int times){
        Size size = new Size(src.width()*times, src.height()*times);
        Mat dst = new Mat(size, src.type());
        Imgproc.resize(src, dst, size);
        return dst;
    }

    public static void drawCirclesOnImage(Mat src, Mat circles){
        Scalar green = new Scalar(0,255, 0);
        drawCirclesOnImage(src, circles, green);
    }

    public static void drawCirclesOnImage(Mat src, Mat circles, Scalar color) {
        for(int i = 0; i < circles.cols(); i++){
            Circle circle = new Circle(circles.get(0, i));
            Point center = circle.getCenter();
            double radius = circle.getRadius();

            Imgproc.circle(src, center, (int)Math.round(radius), color, 6, Imgproc.LINE_AA, 0);
        }
    }

    public static void drawPointsOnImage(Mat src, Point[] points){
        Scalar red = new Scalar(0, 0, 255);
        int size = points.length;
        Point3[] circles = new Point3[size];
        Point point;
        for (int i=0; i<size; i++){
            point = points[i];
            circles[i] = new Point3(point.x, point.y, 1);
        }
        drawCirclesOnImage(src, new MatOfPoint3(circles).t(), red);
    }

}
