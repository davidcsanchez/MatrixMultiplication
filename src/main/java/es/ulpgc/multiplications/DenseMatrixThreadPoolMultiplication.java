package es.ulpgc.multiplications;

import es.ulpgc.Matrix;
import es.ulpgc.MatrixException;
import es.ulpgc.Multiplication;
import es.ulpgc.matrices.DenseMatrix;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DenseMatrixThreadPoolMultiplication implements Multiplication {

    private static ExecutorService executorService;
    private static double[][] result;

    @Override
    public Matrix execute(Matrix a, Matrix b) {
        checkIsDenseMatrix(a);
        checkIsDenseMatrix(b);
        executorService = Executors.newFixedThreadPool(a.size());
        result = new double[a.size()][a.size()];
        for (int i = 0; i < a.size(); i++) submit(a, b, a.size(), i);
        try {
            executorService.shutdown();
            executorService.awaitTermination(1000, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
        return new DenseMatrix(result);
    }

    private void submit(Matrix a, Matrix b, int size, int i) {
        executorService.submit(() -> {
            for (int k = 0; k < size; k++)
                for (int j = 0; j < size; j++)
                    result[i][j] += a.value(i, k) * b.value(k, j);
        });
    }

    private void checkIsDenseMatrix(Matrix matrix) {
        if (matrix instanceof DenseMatrix) return;
        throw new MatrixException("Supplied Matrix is of unsupported type");
    }
}
