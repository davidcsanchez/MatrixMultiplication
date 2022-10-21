package es.ulpgc.multiplications;

import es.ulpgc.Matrix;
import es.ulpgc.MatrixException;
import es.ulpgc.Multiplication;
import es.ulpgc.matrices.DenseMatrix;

import java.util.Arrays;
import java.util.stream.IntStream;

public class DenseMatrixParallelStreamMultiplication implements Multiplication {

    @Override
    public Matrix execute(Matrix a, Matrix b) {
        checkisDenseMatrix(a);
        checkisDenseMatrix(b);
        return new DenseMatrix(Arrays.stream(a.raw()).parallel()
                .map(row -> IntStream.range(0, b.size())
                        .mapToDouble(i -> IntStream.range(0, b.size())
                                .mapToDouble(j -> row[j] * b.value(j, i)).sum())
                        .toArray())
                .toArray(double[][]::new));
    }

    private void checkisDenseMatrix(Matrix matrix) {
        if (matrix instanceof DenseMatrix) return;
        throw new MatrixException("Supplied Matrix is of unsupported type");
    }
}
