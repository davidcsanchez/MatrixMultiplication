package es.ulpgc.transposers;

import es.ulpgc.Matrix;
import es.ulpgc.MatrixTransposer;
import es.ulpgc.matrices.DenseMatrix;

public class DenseMatrixTransposer implements MatrixTransposer {
    @Override
    public Matrix execute(Matrix matrix) {
        int size = matrix.size();
        double[][] transposed = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                transposed[j][i] = matrix.value(i, j);
            }
        }
        return new DenseMatrix(transposed);
    }
}
