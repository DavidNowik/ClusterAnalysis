package Analysis;

import KMeansHighDimensional.Cluster;
import KMeansHighDimensional.DataPoint;

import java.util.*;

public class Silhouette {
    public static String debugReductionString = "default";

    public static double computeSilhouette(List<Cluster> clusters) {

        List<DataPoint> allPoints = new ArrayList<>();
        Map<Integer, Integer> pointToCluster = new HashMap<>();

        for (int i = 0; i < clusters.size(); i++) {
            for (DataPoint p : clusters.get(i).getPoints()) {
                allPoints.add(p);
                pointToCluster.put(p.getLabel(), i);
            }
        }

        double totalScore = 0;

        for (DataPoint p : allPoints) {

            int clusterId = pointToCluster.get(p.getLabel());

            double a = avgDistanceToCluster(p, clusters.get(clusterId));

            double b = Double.MAX_VALUE;

            for (int i = 0; i < clusters.size(); i++) {
                if (i == clusterId) continue;

                double dist = avgDistanceToCluster(p, clusters.get(i));
                b = Math.min(b, dist);
            }

            double s;

            if (a < b) {
                s = 1 - (a / b);
            } else if (a > b) {
                s = (b / a) - 1;
            } else {
                s = 0;
            }

            totalScore += s;
        }

        System.out.println("totalScore "+totalScore+" size "+allPoints.size());
        return totalScore / allPoints.size();
    }

    private static double avgDistanceToCluster(DataPoint p, Cluster cluster) {

        double sum = 0;
        int count = 0;

        for (DataPoint q : cluster.getPoints()) {
            if (p.getLabel() == q.getLabel()) continue;

            sum += distance(p.getFeatures(), q.getFeatures());
            count++;
        }

        return (count == 0) ? 0 : sum / count;
    }

    private static double distance(double[] a, double[] b) {

        double sum = 0;

        for (int i = 0; i < a.length; i++) {
            double diff = a[i] - b[i];
            sum += diff * diff;
        }

        return Math.sqrt(sum);
    }
}