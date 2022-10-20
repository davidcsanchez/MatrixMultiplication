package es.ulpgc.builders;

import es.ulpgc.Matrix;
import es.ulpgc.MatrixBuilder;
import es.ulpgc.matrices.DenseMatrix;

public class DenseMatrixBuilder implements MatrixBuilder {

    private double[][] raw;

    public DenseMatrixBuilder(int size) {
        raw = new double[size][size];
    }

    @Override
    public Matrix build() {
        return new DenseMatrix(raw);
    }

    @Override
    public void set(int row, int col, double val) {
        raw[row][col] = val;
    }

    @Override
    public void set(Matrix matrix) {
        raw = matrix.raw();
    }
}
