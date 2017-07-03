package main.Utils;

import org.opencv.core.Point;
import org.opencv.core.Point3;

/**
 * Created by Magda on 03/07/2017.
 */
public class Circle {

    public Circle(Point3 circle) {
        this.circle = circle;
    }

    public Circle(double[] circle) {
        this.circle = new Point3(circle);
    }

    public Point3 toPoint3() {
        return circle;
    }

    public void setCircle(Point3 circle) {
        this.circle = circle;
    }

    private Point3 circle;


    public double getRadius() {
        return circle.z;
    }

    public Point getCenter() {
        return new Point(circle.x, circle.y);
    }

}
