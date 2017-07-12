package main.normaliser;

import main.utils.Circle;
import main.utils.FilterConstants;
import org.opencv.core.Point;

class CoordinateConverter {

    //TODO CoordinateConverterTest

    private static final FilterConstants filterStats = new FilterConstants();

    //https://www.ripublication.com/gjbmit/gjbmitv1n2_01.pdf -> publication with equations for normalisation

    private static double adjustR(double r) {
        //normalisation because r is in range [0;rows*size] but the equation uses range [0;1]
        r = r / filterStats.getTotalRows();
        assert r >= 0 && r <= 1;
        return r;
    }

    private static double adjustTh(double th) {
        //because th is in range [0;cols*size] but the equation uses range [0;2*pi] (radians)
        th = 2 * Math.PI * th / filterStats.getTotalCols();
        //https://stackoverflow.com/questions/24234609/standard-way-to-normalize-an-angle-to-%CF%80-radians-in-java         //TODO remove this comment
        //th = th - TWO_PI * Math.floor((th + Math.PI) / TWO_PI);         //TODO remove this comment
        assert th >= 0 && th <= 2 * Math.PI;
        return th;
    }

    static Point toXY(double r, double th, Circle pupil, Circle iris) {
        r = adjustR(r);
        th = adjustTh(th);

        Point pupilPoint = iris.pointAtAngle(th, pupil.getRadius());
        Point irisPoint = iris.pointAtAngle(th);
        double x = (1 - r) * pupilPoint.x + r * irisPoint.x;
        double y = (1 - r) * pupilPoint.y + r * irisPoint.y;
        return new Point(x, y);
    }
}