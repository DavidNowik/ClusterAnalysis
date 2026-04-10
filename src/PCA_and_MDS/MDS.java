package PCA_and_MDS;

import Analysis.FileParser;
import KMeansHighDimensional.DataPoint;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MDS {


    //CAREFUL recution algorithms in this implementation EXPECT A LABEL
    //giving a datalist without label can reduce accuracy
    public static List<DataPoint> reduce(List<DataPoint> data, int targetDim) {
        int n = data.size();

        // 1. Compute squared distance matrix
        double[][] D2 = new double[n][n];
        for (int i = 0; i < n; i++) {
            double[] xi = data.get(i).getFeatures();
            for (int j = i; j < n; j++) {
                double[] xj = data.get(j).getFeatures();
                double dist2 = 0;
                for (int k = 0; k < xi.length; k++) dist2 += Math.pow(xi[k] - xj[k], 2);
                D2[i][j] = dist2;
                D2[j][i] = dist2;
            }
        }

        // 2. Double-centering: B = -0.5 * J * D2 * J
        RealMatrix D2Matrix = MatrixUtils.createRealMatrix(D2);
        RealMatrix J = MatrixUtils.createRealIdentityMatrix(n).scalarAdd(-1.0 / n); // J = I - 11^T/n
        RealMatrix B = J.multiply(D2Matrix).multiply(J).scalarMultiply(-0.5);

        // 3. Eigen-decomposition
        EigenDecomposition eigen = new EigenDecomposition(B);
        double[] eigenValues = eigen.getRealEigenvalues();
        RealMatrix V = eigen.getV();

        // 4. Sort eigenvalues and eigenvectors descending
        Integer[] idx = new Integer[eigenValues.length];
        for (int i = 0; i < idx.length; i++) idx[i] = i;
        Arrays.sort(idx, (i, j) -> Double.compare(eigenValues[j], eigenValues[i]));

        // 5. Take top targetDim eigenvectors
        double[][] coords = new double[n][targetDim];
        for (int i = 0; i < targetDim; i++) {
            int j = idx[i];
            double sqrtLambda = Math.sqrt(Math.max(eigenValues[j], 0)); // avoid negative due to rounding
            double[] vCol = V.getColumn(j);
            for (int k = 0; k < n; k++) coords[k][i] = vCol[k] * sqrtLambda;
        }

        // 6. Convert back to DataPoints
        List<DataPoint> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            result.add(new DataPoint(coords[i], true));
        }
        FileParser.writeDataPointsToCSV(result, "reduced.csv");

        return result;
    }
}
