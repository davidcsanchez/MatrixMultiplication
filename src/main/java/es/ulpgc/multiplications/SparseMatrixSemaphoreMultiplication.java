package es.ulpgc.multiplications;

import es.ulpgc.Matrix;
import es.ulpgc.MatrixException;
import es.ulpgc.Multiplication;
import es.ulpgc.matrices.DenseMatrix;
import es.ulpgc.matrices.SparseMatrix;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SparseMatrixSemaphoreMultiplication implements Multiplication {

    private static ExecutorService executorService;
    private Semaphore semaphore;
    private double[][] result;

    @Override
    public Matrix execute(Matrix a, Matrix b) {
        checkIsSparseMatrix(a);
        checkIsSparseMatrix(b);
        int size = a.size();
        double[][] aValues = a.raw();
        double[][] bValues = b.raw();
        semaphore = new Semaphore(1);
        executorService = Executors.newFixedThreadPool(size);
        result = new double[size][size];
        for (int i = 0; i < size; i++)
            for (int k = 0; k < size; k++)
                for (int j = 0; j < size; j++)
                    submit(aValues, bValues, k, i, j);
        try {
            executorService.shutdown();
            executorService.awaitTermination(1000, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
        return new DenseMatrix(result);
    }

    private void submit(double[][] a, double[][] b, int k, int i, int j) {
        executorService.submit(() -> {
            try {
                if (a[i][k] == 0 || b[k][j] == 0) return;
                double value = a[i][k] * b[k][j];
                semaphore.acquire();
                result[i][j] += value;
                semaphore.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void checkIsSparseMatrix(Matrix matrix) {
        if (matrix instanceof SparseMatrix) return;
        throw new MatrixException("Supplied Matrix is of unsupported type");
    }
}
