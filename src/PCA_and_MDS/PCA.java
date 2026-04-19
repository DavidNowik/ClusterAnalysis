package PCA_and_MDS;

import Analysis.FileParser;
import KMeansHighDimensional.DataPoint;
import org.apache.commons.math3.linear.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

//Principal Component Analysis
public class PCA {

    //CAREFUL recution algorithms in this implementation EXPECT A LABEL
    //giving a datalist without label can reduce accuracy
    public static List<DataPoint> reduce(List<DataPoint> data, int targetDim, boolean hasLabel) {

        int n = data.size();
        int d = data.get(0).size();

        if (targetDim > d) {
            throw new IllegalArgumentException("Target dimension must be <= original dimension.");
        }

        // ---------- 1. Build data matrix ----------
        double[][] matrix = new double[n][d];

        for (int i = 0; i < n; i++) {
            matrix[i] = data.get(i).getFeatures();
        }

        RealMatrix X = MatrixUtils.createRealMatrix(matrix);

        // ---------- 2. Mean center ----------
        double[] mean = new double[d];

        for (int j = 0; j < d; j++) {
            double sum = 0;
            for (int i = 0; i < n; i++) {
                sum += matrix[i][j];
            }
            mean[j] = sum / n;
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < d; j++) {
                matrix[i][j] -= mean[j];
            }
        }

        X = MatrixUtils.createRealMatrix(matrix);

        // ---------- 3. Covariance matrix ----------
        RealMatrix covariance = X.transpose().multiply(X).scalarMultiply(1.0 / (n - 1));

        // ---------- 4. Eigen decomposition ----------
        EigenDecomposition eigen = new EigenDecomposition(covariance);
        printExplainedVariance(eigen);

        RealMatrix eigenVectors = eigen.getV();

        // ---------- 5. Select top components ----------
        RealMatrix projectionMatrix =
                eigenVectors.getSubMatrix(0, d - 1, d - targetDim, d - 1);

        // ---------- 6. Project data ----------
        RealMatrix reduced = X.multiply(projectionMatrix);

        // ---------- 7. Convert back to DataPoints ----------
        List<DataPoint> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            double[] features = reduced.getRow(i);
            if(hasLabel){
                int label = data.get(i).getLabel();
                result.add(new DataPoint(features, label));
            }
            else
                result.add(new DataPoint(features, false));

        }
        FileParser.writeDataPointsToCSV(result, "reduced.csv");
        System.out.println("Print example datapoint "+ result.get(0));
        return result;
    }
    private static void printExplainedVariance(EigenDecomposition eigen) {

        double[] eigenValues = eigen.getRealEigenvalues();

        // Summe aller Eigenwerte
        double total = 0;
        for (double v : eigenValues) {
            total += v;
        }

        // sortieren (aufsteigend)
        java.util.Arrays.sort(eigenValues);

        System.out.println("Explained Variance Ratio:");

        double cumulative = 0;

        // von groß nach klein durchgehen
        for (int i = eigenValues.length - 1; i >= 0; i--) {

            double ratio = eigenValues[i] / total;
            cumulative += ratio;

        }
    }
}