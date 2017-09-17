package main.normaliser;

import main.utils.Circle;
import org.opencv.core.Point;

class CoordinateConverter {

    //https://www.ripublication.com/gjbmit/gjbmitv1n2_01.pdf -> publication with equations for normalisation

    private static double adjustR(double r, double height) {
        //normalisation because r is in range [0;rows*size] but the equation uses range [0;1]
        r = r / height;
        assert r >= 0 && r <= 1;
        return r;
    }

    private static double adjustTh(double th, double width) {
        //because th is in range [0;cols*size] but the equation uses range [0;2*pi] (radians)
        th = 2. * Math.PI * th / width;
        assert th >= 0 && th <= 2 * Math.PI;
        return th;
    }

    static Point toXY(double r, double th, Circle pupil, Circle iris, int width, int height) {
        r = adjustR(r, height);
        th = adjustTh(th, width);

        Point pupilPoint = pupil.pointAtAngle(th);
        Point irisPoint = iris.pointAtAngle(th);
        double x = (1 - r) * pupilPoint.x + r * irisPoint.x;
        double y = (1 - r) * pupilPoint.y + r * irisPoint.y;
        return new Point(x, y);
    }
}