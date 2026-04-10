package Analysis;

import KMeansHighDimensional.Cluster;
import KMeansHighDimensional.DataPoint;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClusterIO {

    public static List<Cluster> readClustersFromJSON(String filename, String subfolder) {

        File file;

        if (subfolder == null || subfolder.isEmpty()) {
            file = new File(getProjectRoot()
                    + File.separator + "materials"
                    + File.separator + "out",
                    filename);
        } else {
            file = new File(getProjectRoot()
                    + File.separator + "materials"
                    + File.separator + "out"
                    + File.separator + subfolder,
                    filename);
        }

        if (!file.exists()) {
            throw new RuntimeException("Have you created ClusterLists first? Run CreateClusterLists.java first \nFile not found: " + file.getAbsolutePath());
        }

        try {
            String content = readAll(file);
            return parseClusters(content);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ---------------- CORE PARSER ----------------

    private static List<Cluster> parseClusters(String text) {

        List<Cluster> clusters = new ArrayList<>();

        // split by clusters (safe because your format is consistent)
        String[] clusterBlocks = text.split("\\{\\s*\"clusterIndex\"");

        for (int i = 1; i < clusterBlocks.length; i++) {

            String block = clusterBlocks[i];

            Cluster cluster = new Cluster(null);
            List<DataPoint> points = new ArrayList<>();

            int pointsStart = block.indexOf("\"points\"");
            if (pointsStart == -1) continue;

            String pointsSection = block.substring(pointsStart);

            // extract all feature-label objects
            String[] pointBlocks = pointsSection.split("\\{\\s*\"features\"");

            for (int j = 1; j < pointBlocks.length; j++) {

                String p = pointBlocks[j];

                double[] features = parseFeatures(p);
                int label = parseLabel(p);

                points.add(new DataPoint(features, label));
            }

            for (DataPoint p : points) {
                cluster.addPoint(p);
            }

            clusters.add(cluster);
        }

        return clusters;
    }

    // ---------------- FEATURES ----------------

    private static double[] parseFeatures(String s) {

        int start = s.indexOf("[");
        int end = s.indexOf("]");

        if (start == -1 || end == -1) return new double[0];

        String inside = s.substring(start + 1, end);

        String[] parts = inside.split(",");

        double[] result = new double[parts.length];

        for (int i = 0; i < parts.length; i++) {
            result[i] = Double.parseDouble(parts[i].trim());
        }

        return result;
    }

    // ---------------- LABEL ----------------

    private static int parseLabel(String s) {

        int idx = s.indexOf("\"label\":");
        if (idx == -1) return -1;

        String sub = s.substring(idx + 8).trim();

        StringBuilder sb = new StringBuilder();

        for (char c : sub.toCharArray()) {
            if (Character.isDigit(c) || c == '-') {
                sb.append(c);
            } else {
                break;
            }
        }

        if (sb.length() == 0) return -1;

        return Integer.parseInt(sb.toString());
    }

    // ---------------- FILE IO ----------------

    private static String readAll(File file) throws IOException {

        StringBuilder sb = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        }

        return sb.toString();
    }

    private static String getProjectRoot() {
        return new File("").getAbsolutePath();
    }
}