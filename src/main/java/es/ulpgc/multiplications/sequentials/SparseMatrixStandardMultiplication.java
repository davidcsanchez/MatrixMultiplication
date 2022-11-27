package es.ulpgc.multiplications.sequentials;

import es.ulpgc.Matrix;
import es.ulpgc.MatrixException;
import es.ulpgc.Multiplication;
import es.ulpgc.builders.SparseMatrixBuilder;
import es.ulpgc.matrices.SparseMatrix;

public class SparseMatrixStandardMultiplication implements Multiplication {
    @Override
    public Matrix execute(Matrix a, Matrix b) {
        checkIsSparseMatrix(a);
        checkIsSparseMatrix(b);
        a = Matrix.create(a.raw());
        b = Matrix.create(b.raw());
        double sum;
        SparseMatrixBuilder builder = new SparseMatrixBuilder(a.size());
        for (int i = 0; i < a.size(); i++)
            for (int j = 0; j < a.size(); j++) {
                sum = 0;
                for (int k = 0; k < a.size(); k++) {
                    if (a.value(i, k) == 0 || b.value(k, j) == 0) continue;
                    sum += a.value(i, k) * b.value(k, j);
                }
                if (sum != 0) builder.set(i, j, sum);
            }
        return builder.build();
    }

    private void checkIsSparseMatrix(Matrix matrix) {
        if (matrix instanceof SparseMatrix) return;
        throw new MatrixException("Supplied Matrix is of unsupported type");
    }
}
