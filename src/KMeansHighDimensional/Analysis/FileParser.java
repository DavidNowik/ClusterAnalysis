package KMeansHighDimensional.Analysis;

import KMeansHighDimensional.DataPoint;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileParser {

    public static void main(String[] args){
        printLoadFiles("arrhythmia.data");
    }
    private static String getProjectRoot() {
        return System.getProperty("user.dir");
    }
    private static void printLoadFiles(String filename){
        List<DataPoint> list = loadCSVorData(filename);
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
    public static List<DataPoint> loadCSVorData(String filename) {
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
                data.add(new DataPoint(features, label));
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

            System.out.println("CSV written to: " + file.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}