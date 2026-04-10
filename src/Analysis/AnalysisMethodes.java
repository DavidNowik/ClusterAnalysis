package Analysis;

import KMeansHighDimensional.Cluster;
import KMeansHighDimensional.DataPoint;

import java.util.List;

public class AnalysisMethodes {

    private static boolean printPointsFully = false;
    public static void printMaxDistance(List<DataPoint> points) {
        if (points.size() < 2) {
            System.out.println("Not enough points to calculate distances.");
            return;
        }

        double maxDist = 0;
        DataPoint p1 = null;
        DataPoint p2 = null;

        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                double dist = points.get(i).distanceTo(points.get(j));
                if (dist > maxDist) {
                    maxDist = dist;
                    p1 = points.get(i);
                    p2 = points.get(j);
                }
            }
        }

        System.out.println("Maximum distance between any two points: " + maxDist);
        if(printPointsFully)
            System.out.println("Between points: \n" + p1 + "\n" + p2);
    }

    public static void printMaxNearestNeighborDistance(List<DataPoint> points) {
        if (points.size() < 2) {
            System.out.println("Not enough points to calculate distances.");
            return;
        }

        double maxNearestDist = 0;
        DataPoint mostIsolated = null;

        for (DataPoint p : points) {
            double nearestDist = Double.MAX_VALUE;

            for (DataPoint other : points) {
                if (p == other) continue;
                double dist = p.distanceTo(other);
                if (dist < nearestDist) nearestDist = dist;
            }

            if (nearestDist > maxNearestDist) {
                maxNearestDist = nearestDist;
                mostIsolated = p;
            }
        }




        System.out.println("Most isolated point (max distance to nearest neighbor): " +
                (printPointsFully ? mostIsolated : "Analysis.printPointsFully is off"));
        System.out.println("Distance to its nearest neighbor: " +
                (printPointsFully ? maxNearestDist : "Analysis.printPointsFully is off"));
    }

    public static void printMinNearestNeighborDistance(List<DataPoint> points) {
        if (points.size() < 2) {
            System.out.println("Not enough points to calculate distances.");
            return;
        }

        double minNearestDist = Double.MAX_VALUE;
        DataPoint closestPoint = null;

        for (DataPoint p : points) {
            double nearestDist = Double.MAX_VALUE;

            for (DataPoint other : points) {
                if (p == other) continue;
                double dist = p.distanceTo(other);
                if (dist < nearestDist) nearestDist = dist;
            }

            if (nearestDist < minNearestDist) {
                minNearestDist = nearestDist;
                closestPoint = p;
            }
        }
        System.out.println("Point with smallest distance to nearest neighbor: " +
                (printPointsFully ? closestPoint : "Analysis.printPointsFully is off"));
        System.out.println("Distance to its nearest neighbor: " + minNearestDist);
    }

    /**
     * Computes the proportion of points that stay in the same cluster
     * between two clustering attempts.
     *
     * @param clusters1 List of clusters from first attempt
     * @param clusters2 List of clusters from second attempt
     * @return proportion of points that remain in the same cluster (0..1)
     */
    public static double computeClusterStability(List<Cluster> clusters1, List<Cluster> clusters2) {
        if (clusters1.size() != clusters2.size()) {
            throw new IllegalArgumentException("Both cluster lists must have the same number of clusters.");
        }

        int totalPoints = 0;
        int sameClusterCount = 0;

        // Map each point to its cluster index for clusters1
        java.util.Map<DataPoint, Integer> pointToCluster1 = new java.util.HashMap<>();
        for (int i = 0; i < clusters1.size(); i++) {
            for (DataPoint p : clusters1.get(i).getPoints()) {
                pointToCluster1.put(p, i);
            }
        }

        // Compare with clusters2
        for (int i = 0; i < clusters2.size(); i++) {
            for (DataPoint p : clusters2.get(i).getPoints()) {
                totalPoints++;
                Integer clusterIndex1 = pointToCluster1.get(p);
                if (clusterIndex1 != null && clusterIndex1 == i) {
                    sameClusterCount++;
                }
            }
        }

        return totalPoints == 0 ? 0 : ((double) sameClusterCount) / totalPoints;
    }


}