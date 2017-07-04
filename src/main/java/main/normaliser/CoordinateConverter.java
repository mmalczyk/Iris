package main.normaliser;

import main.Utils.Circle;
import org.opencv.core.Point;

public class CoordinateConverter {

    //https://www.ripublication.com/gjbmit/gjbmitv1n2_01.pdf -> publication with equations for normalisation

    private static double adjustR(double r){
        //because r is in range [0;100] but the equation uses range [0;1]
        r = r/100.;
        assert r>=0 && r<=1;
        return r;
    }

    private static double adjustTh(double th){
        //because th is in range [0;200*pi] but the equation uses range [0;2*pi] (radians)
        th = th/100.;
        assert th>=0 && th<=2*Math.PI;
        return th;
    }

    public static Point toXY(double r, double th, Circle pupil, Circle iris) {
        r = adjustR(r);
        th = adjustTh(th);

        Point pupilPoint = iris.pointAtAngle(th, pupil.getRadius());
        Point irisPoint = iris.pointAtAngle(th);
        double x = (1 - r) * pupilPoint.x + r * irisPoint.x;
        double y = (1 - r) * pupilPoint.y + r * irisPoint.y;
        return new Point(x, y);
    }
}