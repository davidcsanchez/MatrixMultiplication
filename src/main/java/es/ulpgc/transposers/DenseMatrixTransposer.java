package es.ulpgc.transposers;

import es.ulpgc.Matrix;
import es.ulpgc.MatrixTransposer;
import es.ulpgc.matrices.DenseMatrix;

public class DenseMatrixTransposer implements MatrixTransposer {
    @Override
    public Matrix execute(Matrix matrix) {
        double[][] transposed = new double[matrix.size()][matrix.size()];
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.size(); j++) {
                transposed[j][i] = matrix.value(i, j);
            }
        }
        return new DenseMatrix(transposed);
    }
}
