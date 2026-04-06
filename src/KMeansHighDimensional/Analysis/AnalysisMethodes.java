package KMeansHighDimensional.Analysis;

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


}