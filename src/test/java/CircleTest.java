import main.Utils.Circle;
import main.Utils.ImageData;
import main.Utils.ImageUtils;
import main.interfaces.ILocaliser;
import main.interfaces.INormaliser;
import main.interfaces.IReader;
import main.localiser.OpenCVLocaliser;
import main.normaliser.OpenCVNormaliser;
import main.reader.SimpleReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint3;
import org.opencv.core.Point;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.opencv.imgcodecs.Imgcodecs.imwrite;

/**
 * Created by Magda on 03/07/2017.
 */
public class CircleTest {

    private static ImageData imageData;
    private static ImageUtils imageUtils;

    @BeforeClass
    public static void runBeforeClass(){
        imageUtils = new ImageUtils(true);
    }

    @Before
    public void runBeforeTestMethod() {
        //TODO I need a test independent from ILocaliser
        IReader reader = new SimpleReader();
        ILocaliser localiser = new OpenCVLocaliser();
        INormaliser normaliser = new OpenCVNormaliser();

        Path path = Paths.get(TestDirectory.images.toString(), "S5000L00.jpg");
        ImageData image = reader.read(path);
        image = localiser.localise(image);
        imageData = normaliser.normalize(image);
    }

    @Test
    public void pointAtAngleTest(){
        Mat image = imageData.getImageMat();
        Circle pupil = imageData.getPupilCircle();
        Circle iris = imageData.getIrisCircle();

        Point[] points = new Point[8];

        points[0] = iris.pointAtAngle(0.);
        points[1] = iris.pointAtAngle(Math.PI/2.);
        points[2] = iris.pointAtAngle(Math.PI);
        points[3] = iris.pointAtAngle(3.*Math.PI/2.);
        points[4] = pupil.pointAtAngle(0.);
        points[5] = pupil.pointAtAngle(Math.PI/2.);
        points[6] = pupil.pointAtAngle(Math.PI);
        points[7] = pupil.pointAtAngle(3.*Math.PI/2.);

        Assert.assertTrue(points[0].equals(new Point(326.05477142333984, 263.0)));
        Assert.assertTrue(points[1].equals(new Point(253.0, 336.05477142333984)));
        Assert.assertTrue(points[2].equals(new Point(179.94522857666016, 263.0)));
        Assert.assertTrue(points[3].equals(new Point(253.0, 189.94522857666016)));
        Assert.assertTrue(points[4].equals(new Point(287.82097244262695, 263.0)));
        Assert.assertTrue(points[5].equals(new Point(253.0, 297.82097244262695)));
        Assert.assertTrue(points[6].equals(new Point(218.17902755737305, 263.0)));
        Assert.assertTrue(points[7].equals(new Point(253.0, 228.17902755737305)));

        imageUtils.drawCirclesOnImage(
                image,
                new MatOfPoint3(pupil.toPoint3(), iris.toPoint3()).t());

        imageUtils.drawPointsOnImage(image, points);

        Path path = Paths.get(TestDirectory.results.toString(), "pointAtAngleTest.jpg");
        imwrite(path.toString(), image);
    }


}
