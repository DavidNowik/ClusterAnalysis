public class Point {

    public double x;
    public double y;

    public int cluster;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
        this.cluster = -1;
    }

    public static double distance(Point a, Point b) {
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}