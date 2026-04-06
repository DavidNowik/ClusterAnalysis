package PCA;

import KMeansHighDimensional.Analysis.ConsoleLogger;
import KMeansHighDimensional.Cluster;
import KMeansHighDimensional.DataPoint;
import KMeansHighDimensional.Analysis.FileParser;
import KMeansHighDimensional.KMeans;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        ConsoleLogger.redirectConsoleOutput("PCA_targetDim_20_NoA100");
        execute();
    }
    private static void writeReducedDataToFile(){


        int k = 10;
        int iterations = 50;
        int targetDimension = 5;   // PCA output dimension

        List<DataPoint> data = FileParser.loadCSVorData("training.csv");

        List<DataPoint> reducedData = PCA.reduce(data, targetDimension);
        FileParser.writeDataPointsToCSV(reducedData, "reducedData.csv");

    }

    private static void execute(){

        int k = 10;
        int iterations = 50;
        int NUMBER_OF_ANALYSES = 100;
        int targetDimension = 20;   // PCA output dimension

        System.out.println("Executing:");
        System.out.println("k = " + k);
        System.out.println("iterations = " + iterations);
        System.out.println("NUMBER_OF_ANALYSES = " + NUMBER_OF_ANALYSES);
        System.out.println("PCA target dimension = " + targetDimension);

        for (int j = 0; j < NUMBER_OF_ANALYSES; j++) {

            List<DataPoint> data = FileParser.loadCSVorData("training.csv");

            List<DataPoint> reducedData = PCA.reduce(data, targetDimension);

            List<Cluster> clusters = KMeans.run(reducedData, k, iterations);

            System.out.println("ATTEMPT: " + (j + 1));

            int i = 1;
            for (Cluster c : clusters) {
                System.out.println("Cluster " + i + " size: " + c.size());
                i++;
            }
            System.out.println();
        }
    }
}