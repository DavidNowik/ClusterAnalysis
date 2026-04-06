package KMeansHighDimensional;

import KMeansHighDimensional.Analysis.ClusterPlot;
import KMeansHighDimensional.Analysis.FileParser;

import java.util.List;

public class Main {

    public static void main(String[] args) {


        int k = 10;
        int iterations = 50;
        int i = 1;
        int NUMBER_OF_ANALYSES = 10;

        System.out.println("Executing:");
        System.out.println("k = " + k);
        System.out.println("iterations = " + iterations);
        System.out.println("i = " + i);
        System.out.println("NUMBER_OF_ANALYSES = " + NUMBER_OF_ANALYSES);

        for (int j = 0; j < NUMBER_OF_ANALYSES; j++) {


            List<DataPoint> data = FileParser.loadCSVorData("training.csv");


            List<Cluster> clusters = KMeans.run(data, k, iterations);

            System.out.println("ATTEMPT: "+(j+1));
            for (Cluster c : clusters) {
                System.out.println("Cluster " + i + " size: " + c.size());
                i++;
            }

            ClusterPlot.show(clusters);

        }

        //AnalysisMethodes.printMaxDistance(data);
        //AnalysisMethodes.printMaxNearestNeighborDistance(data);
        //AnalysisMethodes.printMinNearestNeighborDistance(data);
    }

}