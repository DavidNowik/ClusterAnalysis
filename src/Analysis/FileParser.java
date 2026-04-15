package Analysis;

import KMeansHighDimensional.Cluster;
import KMeansHighDimensional.ClusterGroup;
import KMeansHighDimensional.DataPoint;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileParser {
    private static boolean debug = false;
    private static String getProjectRoot() {
        return System.getProperty("user.dir");
    }
    private static void printLoadFiles(String filename, boolean hasLabel){
        List<DataPoint> list = loadCSVorData(filename, hasLabel);
        System.out.println("__________ PRINTING "+filename+" __________");
        if(list.isEmpty()){
            System.out.println("List extracted from "+filename+" is empty");
        }
        for(DataPoint d : list){
            System.out.println(d.toString());
        }
    }

    private static File getFileInMaterials(String filename){
        File file = new File(getProjectRoot() + File.separator + "materials" + File.separator + filename);
        if (!file.exists()) {
            System.out.println("Datei nicht gefunden: " + file.getAbsolutePath());
        }
        return file;
    }
    public static List<DataPoint> loadCSVorData(String filename, boolean hasLabel) {
        List<DataPoint> data = new ArrayList<>();
        File file = getFileInMaterials(filename);

        if (!file.exists()) {
            System.out.println("Datei existiert nicht: " + file.getAbsolutePath());
            return data;
        }

        String debug = "NOTHING_READ";

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            br.mark(1000);      // remember current position (up to 1000 chars)
            String line = br.readLine();
            if( Character.isDigit(line.charAt(0))){
                br.reset();         // go back to where mark() was called
            }
            while ((line = br.readLine()) != null) {
                debug = line;
                String adjustedLine = line.replace('?', '0');
                String[] parts = adjustedLine.split(",");
                int n = parts.length;
                double[] features = new double[n - 1];
                for (int i = 0; i < n - 1; i++) {
                    features[i] = parseAnything(parts[i]);
                }
                int label = (int) parseAnything(parts[n - 1]);
                data.add(new DataPoint(features, hasLabel));
            }
        } catch (IOException | NumberFormatException ex) {
            System.out.println("Stopped at " + debug);
            ex.printStackTrace();
        }

        return data;
    }
    private static double parseAnything(String word) {
        try {
            return Double.parseDouble(word.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static void writeDataPointsToCSV(List<DataPoint> data, String filename) {

        File outDir = new File(getProjectRoot() + File.separator + "materials" + File.separator + "out");
        if(debug)
            System.out.println("Trying to write to "+outDir.getAbsolutePath());

        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        File file = new File(outDir, filename);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            for (DataPoint p : data) {

                double[] features = p.getFeatures();

                for (int i = 0; i < features.length; i++) {
                    writer.write(String.valueOf(features[i]));
                    writer.write(",");
                }

                writer.write(String.valueOf(p.getLabel()));
                writer.newLine();
            }

            if(debug)
            System.out.println("CSV written to: " + file.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void writeClusterSizesToJSON(List<ClusterGroup> groups, String filename, String subfolder) {

        File outDir;
        if(subfolder.isEmpty()){
            if(debug)
            System.out.println("Has no subfolder");
            outDir = new File(getProjectRoot() + File.separator + "materials" + File.separator + "out");
        } else {
            if(debug)
            System.out.println("Has subfolder");
            outDir = new File(getProjectRoot() + File.separator + "materials" + File.separator + "out" + File.separator + subfolder);
        }
        if(debug)
        System.out.println("Trying to write to " + outDir.getAbsolutePath());

        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        File file = new File(outDir, filename);
        if (!filename.endsWith(".json")) {
            filename += ".json";
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            writer.write("[");
            writer.newLine();

            for (int i = 0; i < groups.size(); i++) {

                ClusterGroup g = groups.get(i);

                writer.write("  {");
                writer.newLine();

                writer.write("    \"reductionType\": \"" + g.getReductionType() + "\",");
                writer.newLine();

                writer.write("    \"attempt\": " + g.getAttempt() + ",");
                writer.newLine();

                writer.write("    \"clusterSizes\": " + g.getClusterSizes().toString());
                writer.newLine();

                writer.write("  }");

                if (i < groups.size() - 1) {
                    writer.write(",");
                }

                writer.newLine();
            }

            writer.write("]");
            writer.newLine();

            if(debug)
                System.out.println("JSON written to: " + file.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeClustersToJSON(List<Cluster> clusters, String filename, String subfolder) {

        File outDir;
        if(subfolder.isEmpty()){
            System.out.println("Has no subfolder");
            outDir = new File(getProjectRoot() + File.separator + "materials" + File.separator + "out");
        } else {
            System.out.println("Has subfolder");
            outDir = new File(getProjectRoot() + File.separator + "materials" + File.separator + "out" + File.separator + subfolder);
        }

        if(debug)
            System.out.println("Trying to write to " + outDir.getAbsolutePath());

        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        if (!filename.endsWith(".json")) {
            filename += ".json";
        }

        File file = new File(outDir, filename);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            writer.write("[");
            writer.newLine();

            for (int i = 0; i < clusters.size(); i++) {
                Cluster c = clusters.get(i);

                writer.write("  {");
                writer.newLine();
                writer.write("    \"clusterIndex\": " + i + ",");
                writer.newLine();
                writer.write("    \"points\": [");

                List<DataPoint> points = c.getPoints();
                for (int j = 0; j < points.size(); j++) {
                    DataPoint p = points.get(j);
                    writer.write("{\"features\": [");

                    double[] features = p.getFeatures();
                    for (int k = 0; k < features.length; k++) {
                        writer.write(String.valueOf(features[k]));
                        if (k < features.length - 1) writer.write(", ");
                    }
                    writer.write("], \"label\": " + p.getLabel() + "}");

                    if (j < points.size() - 1) writer.write(", ");
                }

                writer.write("]");
                writer.newLine();
                writer.write("  }");

                if (i < clusters.size() - 1) writer.write(",");
                writer.newLine();
            }

            writer.write("]");
            writer.newLine();

            if(debug)
                System.out.println("Clusters JSON written to: " + file.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void clearAnalysisFile() {
        try {
            File outDir = new File(getProjectRoot() + File.separator + "materials");

            if (!outDir.exists()) {
                outDir.mkdirs();
            }

            File outFile = new File(outDir, "collected_analysis_data.txt");

            FileWriter fw = new FileWriter(outFile, false); // FALSE = überschreiben!
            fw.write(""); // leer machen
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void appendAnalysisResult(
            String reductionType,
            double avgARI,
            double avgAMI,
            double avgSIL
    ) {
        try {
            File outDir = new File(getProjectRoot() + File.separator + "materials");

            if (!outDir.exists()) {
                outDir.mkdirs();
            }

            File outFile = new File(outDir, "collected_analysis_data.txt");

            FileWriter fw = new FileWriter(outFile, true); // TRUE = append!!
            BufferedWriter bw = new BufferedWriter(fw);

            String line = "reduction=" + reductionType +
                    " | ARI=" + avgARI +
                    " | AMI=" + avgAMI +
                    " | SIL=" + avgSIL;

            bw.write(line);
            bw.newLine();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void writePointsToCSV(List<Cluster> clusters, String filename) {

        File outDir = new File(getProjectRoot() + File.separator + "materials" + File.separator + "out");
        if(debug)
            System.out.println("Trying to write to " + outDir.getAbsolutePath());

        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        if (!filename.endsWith(".csv")) {
            filename += ".csv";
        }

        File file = new File(outDir, filename);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            // optional header
            writer.write("clusterIndex,features,label");
            writer.newLine();

            for (int i = 0; i < clusters.size(); i++) {
                Cluster c = clusters.get(i);

                for (DataPoint p : c.getPoints()) {

                    double[] features = p.getFeatures();

                    writer.write(String.valueOf(i)); // cluster index

                    for (double f : features) {
                        writer.write("," + f);
                    }

                    writer.write("," + p.getLabel());
                    writer.newLine();
                }
            }

            if (debug)
                System.out.println("CSV written to: " + file.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void ensureLabelColumn(String inputFilename, String outputFilename) {

        String fullInput =
                getProjectRoot() + File.separator + "materials" + File.separator + inputFilename;
        String fullOutput =
                getProjectRoot() + File.separator + "materials" + File.separator + outputFilename;
        File inputFile = new File(fullInput);
        File outputFile = new File(fullOutput);

        if(debug)
            System.out.println(fullOutput+" written from "+fullOutput);

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            String header = reader.readLine();
            if (header == null) return;

            String[] columns = header.split(",");
            boolean lastIsLabel = columns[columns.length - 1].equalsIgnoreCase("label");

            if (lastIsLabel) {
                System.out.println(inputFilename+" already has label as last dimension in column");
                return;
            } else {
                // append label column at the end
                writer.write(header + ",label");
                writer.newLine();

                String line;
                while ((line = reader.readLine()) != null) {
                    writer.write(line + ","+labelIndex++); // default label
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static int labelIndex = 1;
}