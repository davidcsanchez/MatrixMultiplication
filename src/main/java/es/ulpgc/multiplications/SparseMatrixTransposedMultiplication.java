package es.ulpgc.multiplications;

import es.ulpgc.Matrix;
import es.ulpgc.MatrixException;
import es.ulpgc.Multiplication;
import es.ulpgc.MatrixTransposer;
import es.ulpgc.builders.SparseMatrixBuilder;
import es.ulpgc.matrices.SparseMatrix;

public class SparseMatrixTransposedMultiplication implements Multiplication {
    private final MatrixTransposer transposer;

    public SparseMatrixTransposedMultiplication(MatrixTransposer transposer) {
        this.transposer = transposer;
    }

    @Override
    public Matrix execute(Matrix a, Matrix b) {
        checkIsSparseMatrix(a);
        checkIsSparseMatrix(b);
        Matrix transposed = transposer.execute(b);
        int size = a.size();
        double[][] aValues = a.raw();
        double[][] tValues = transposed.raw();
        double sum;
        SparseMatrixBuilder builder = new SparseMatrixBuilder(size);
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                sum = 0;
                for (int k = 0; k < size; k++) {
                    if (aValues[i][k] == 0 || tValues[j][k] == 0) continue;
                    sum += aValues[i][k] * tValues[j][k];
                }
                builder.set(i, j, sum);
            }
        return builder.build();
    }

    private void checkIsSparseMatrix(Matrix matrix) {
        if (matrix instanceof SparseMatrix) return;
        throw new MatrixException("Supplied Matrix is of unsupported type.");
    }
}
