package es.ulpgc.multiplications;

import es.ulpgc.Matrix;
import es.ulpgc.MatrixException;
import es.ulpgc.Multiplication;
import es.ulpgc.matrices.DenseMatrix;
import es.ulpgc.matrices.SparseMatrix;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SparseMatrixThreadPoolMultiplication implements Multiplication {

    private static ExecutorService executorService;
    private static double[][] result;

    @Override
    public Matrix execute(Matrix a, Matrix b) {
        checkIsSparseMatrix(a);
        checkIsSparseMatrix(b);
        int size = a.size();
        double[][] aValues = a.raw();
        double[][] bValues = b.raw();
        result = new double[size][size];
        executorService = Executors.newFixedThreadPool(size);
        for (int i = 0; i < size; i++) submit(aValues, bValues, size, i);
        try {
            executorService.shutdown();
            executorService.awaitTermination(1000, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
        return new DenseMatrix(result);
    }

    private void submit(double[][] a, double[][] b, int size, int i) {
        executorService.submit(() -> {
            for (int k = 0; k < size; k++)
                for (int j = 0; j < size; j++) {
                    if (a[i][k] == 0 || b[k][j] == 0) continue;
                    result[i][j] += a[i][k] * b[k][j];
                }
        });
    }

    private void checkIsSparseMatrix(Matrix matrix) {
        if (matrix instanceof SparseMatrix) return;
        throw new MatrixException("Supplied Matrix is of unsupported type.");
    }
}
