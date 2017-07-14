import main.display.Display;
import main.interfaces.IReader;
import main.reader.OpenCVReader;
import main.utils.Circle;
import main.utils.ImageData;
import main.utils.ImageUtils;
import main.utils.TestDirectory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.opencv.imgcodecs.Imgcodecs.imwrite;

/**
 * Created by Magda on 03/07/2017.
 */
public class CircleTest {

    private static ImageData imageData;

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        if (Display.moduleNotInDictionary(OpenCVReader.class))
            Display.displayModule(OpenCVReader.class, false);
    }

    @Before
    public void runBeforeTestMethod() {
        //TODO I need a test independent from ILocaliser
        IReader reader = new OpenCVReader();
        Path path = Paths.get(TestDirectory.images.toString(), "S5000L00.jpg");
        imageData = reader.read(path);
    }

    //TODO test exact angles in case dimensions got switched
    @Test
    public void pointAtAngleTest() {
        Mat image = imageData.getImageMat();
        Circle pupil = new Circle(new double[]{253., 263., 34.5});
        Circle iris = new Circle(new double[]{253., 263., 73.05});

        Point[] points = new Point[8];

        points[0] = iris.pointAtAngle(0.);
        points[1] = iris.pointAtAngle(Math.PI / 2.);
        points[2] = iris.pointAtAngle(Math.PI);
        points[3] = iris.pointAtAngle(3. * Math.PI / 2.);
        points[4] = pupil.pointAtAngle(0.);
        points[5] = pupil.pointAtAngle(Math.PI / 2.);
        points[6] = pupil.pointAtAngle(Math.PI);
        points[7] = pupil.pointAtAngle(3. * Math.PI / 2.);

        Assert.assertTrue(ImageUtils.distance(points[0], new Point(326.05477142333984, 263.0)) < 1.);
        Assert.assertTrue(ImageUtils.distance(points[1], new Point(253.0, 336.05477142333984)) < 1.);
        Assert.assertTrue(ImageUtils.distance(points[2], new Point(179.94522857666016, 263.0)) < 1.);
        Assert.assertTrue(ImageUtils.distance(points[3], new Point(253.0, 189.94522857666016)) < 1.);
        Assert.assertTrue(ImageUtils.distance(points[4], new Point(287.82097244262695, 263.0)) < 1.);
        Assert.assertTrue(ImageUtils.distance(points[5], new Point(253.0, 297.82097244262695)) < 1.);
        Assert.assertTrue(ImageUtils.distance(points[6], new Point(218.17902755737305, 263.0)) < 1.);
        Assert.assertTrue(ImageUtils.distance(points[7], new Point(253.0, 228.17902755737305)) < 1.);

        image = ImageUtils.drawCircles(image, new Circle[]{pupil, iris});

        image = ImageUtils.drawPointsOnImage(image, points);

        Path path = Paths.get(TestDirectory.results.toString(), "pointAtAngleTest.jpg");
        imwrite(path.toString(), image);
    }


}
