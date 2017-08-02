import main.interfaces.IReader;
import main.reader.OpenCVReader;
import main.utils.Circle;
import main.utils.ImageData;
import main.utils.ImageUtils;
import main.utils.TestDirectory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.nio.file.Path;

/**
 * Created by Magda on 03/07/2017.
 */
public class CircleTest extends BaseTest {

    private ImageData imageData;

    @Before
    public void runBeforeTestMethod() {
        IReader reader = new OpenCVReader();
        Path path = TestDirectory.CASIA_Image(0, TestDirectory.Eye.Left, 0);
        imageData = reader.read(path);
    }

    //TODO test exact angles in case dimensions got switched
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void pointAtAngleTest() {
        Mat image = imageData.getImageMat();
        Circle pupil = new Circle(new double[]{253., 263., 34.5});
        Circle iris = new Circle(new double[]{253., 263., 73.05});
        image = ImageUtils.drawCircles(image, new Circle[]{pupil, iris});

        Mat image1 = image.clone();
        Mat image2 = image.clone();
        Mat image3 = image.clone();
        Mat image4 = image.clone();

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
        Assert.assertTrue(ImageUtils.distance(points[4], new Point(287.82097244262695, 263.0)) < 1.);
        image1 = ImageUtils.drawPointsOnImage(image1, new Point[]{points[0], points[4]});

        Assert.assertTrue(ImageUtils.distance(points[1], new Point(253.0, 336.05477142333984)) < 1.);
        Assert.assertTrue(ImageUtils.distance(points[5], new Point(253.0, 297.82097244262695)) < 1.);
        image2 = ImageUtils.drawPointsOnImage(image2, new Point[]{points[1], points[5]});

        Assert.assertTrue(ImageUtils.distance(points[2], new Point(179.94522857666016, 263.0)) < 1.);
        Assert.assertTrue(ImageUtils.distance(points[6], new Point(218.17902755737305, 263.0)) < 1.);
        image3 = ImageUtils.drawPointsOnImage(image3, new Point[]{points[2], points[6]});

        Assert.assertTrue(ImageUtils.distance(points[3], new Point(253.0, 189.94522857666016)) < 1.);
        Assert.assertTrue(ImageUtils.distance(points[7], new Point(253.0, 228.17902755737305)) < 1.);
        image4 = ImageUtils.drawPointsOnImage(image4, new Point[]{points[3], points[7]});

        (new java.io.File(resultsDirectory.toString())).mkdirs();
        ImageUtils.writeToFile(image1, resultsDirectory, "pointAtAngleTest1.jpg");
        ImageUtils.writeToFile(image2, resultsDirectory, "pointAtAngleTest2.jpg");
        ImageUtils.writeToFile(image3, resultsDirectory, "pointAtAngleTest3.jpg");
        ImageUtils.writeToFile(image4, resultsDirectory, "pointAtAngleTest4.jpg");

    }

}
