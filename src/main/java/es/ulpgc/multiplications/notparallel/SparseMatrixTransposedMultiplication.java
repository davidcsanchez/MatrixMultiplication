package es.ulpgc.multiplications.notparallel;

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
        Matrix transposed = transposer.execute(Matrix.create(b.raw()));
        int size = a.size();
        a = Matrix.create(a.raw());
        double sum;
        SparseMatrixBuilder builder = new SparseMatrixBuilder(size);
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                sum = 0;
                for (int k = 0; k < size; k++) {
                    if (a.value(i, k) == 0 || transposed.value(j, k) == 0) continue;
                    sum += a.value(i, k) * transposed.value(j, k);
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
