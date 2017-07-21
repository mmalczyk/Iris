package main.utils;

import org.opencv.core.Point;
import org.opencv.core.Point3;

/**
 * Created by Magda on 03/07/2017.
 */
public class Circle {

    private final Point3 circle;

    public Circle(Point3 circle) {
        this.circle = circle;
    }

    public Circle(double[] circle) {
        this.circle = new Point3(circle);
    }

    public Point3 toPoint3() {
        return circle;
    }

    public double getX() {
        return circle.x;
    }

    public void setX(double x) {
        circle.x = x;
    }

    public double getY() {
        return circle.y;
    }

    public void setY(double y) {
        circle.y = y;
    }

    public double getRadius() {
        return circle.z;
    }

    public void setRadius(double r) {
        circle.z = r;
    }

    public Point getCenter() {
        return new Point(circle.x, circle.y);
    }

    public Point pointAtAngle(double angle) {
        return pointAtAngle(angle, getRadius());
    }

    public Point pointAtAngle(double angle, double radius) {
        Point centerPoint = getCenter();
        Point edgePoint = new Point();
        //Math.cos & Math.sin expect angle in radians
        edgePoint.x = centerPoint.x + radius * Math.cos(angle);
        edgePoint.y = centerPoint.y + radius * Math.sin(angle);
        return edgePoint;
    }

    public Circle copy() {
        return new Circle(this.toPoint3().clone());
    }
}
