package es.ulpgc.transposers;

import es.ulpgc.Matrix;
import es.ulpgc.MatrixTransposer;
import es.ulpgc.builders.SparseMatrixBuilder;

public class SparseMatrixTransposer implements MatrixTransposer {
    @Override
    public Matrix execute(Matrix matrix) {
        SparseMatrixBuilder builder = new SparseMatrixBuilder(matrix.size());
        Matrix m = Matrix.create(matrix.raw());
        for (int i = 0; i < m.size(); i++)
            for (int j = 0; j < m.size(); j++) {
                if (m.value(i, j) == 0) continue;
                builder.set(j, i, m.value(i, j));
            }
        return builder.build();
    }
}
