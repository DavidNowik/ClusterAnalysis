package KMeansHighDimensional;

import java.util.ArrayList;
import java.util.List;

public class Cluster {

    private double[] centroid;
    private List<DataPoint> points;

    public Cluster(double[] centroid) {
        this.centroid = centroid;
        this.points = new ArrayList<>();
    }

    public double[] getCentroid() {
        return centroid;
    }

    public void setCentroid(double[] centroid) {
        this.centroid = centroid;
    }

    public List<DataPoint> getPoints() {
        return points;
    }

    public void addPoint(DataPoint p) {
        points.add(p);
    }

    public void clearPoints() {
        points.clear();
    }

    public int size() {
        return points.size();
    }

    public double distanceTo(DataPoint p) {
        return euclidean(p.getFeatures(), centroid);
    }

    private static double euclidean(double[] a, double[] b) {

        double sum = 0;

        for (int i = 0; i < a.length; i++) {

            double diff = a[i] - b[i];
            sum += diff * diff;

        }

        return Math.sqrt(sum);
    }
}