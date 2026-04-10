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
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("Cluster{\n");

        sb.append("  centroid = ");

        if (centroid == null) {
            sb.append("null");
        } else {
            sb.append("[");
            for (int i = 0; i < centroid.length; i++) {
                sb.append(centroid[i]);
                if (i < centroid.length - 1) sb.append(", ");
            }
            sb.append("]");
        }

        sb.append(",\n  size = ").append(points.size());

        sb.append(",\n  samplePoints = ");

        int limit = Math.min(points.size(), 3); // avoid spam

        sb.append("[");
        for (int i = 0; i < limit; i++) {
            sb.append(points.get(i).getFeaturesString());
            if (i < limit - 1) sb.append(", ");
        }

        if (points.size() > 3) {
            sb.append(", ...");
        }

        sb.append("]\n");

        sb.append("}");

        return sb.toString();
    }
}