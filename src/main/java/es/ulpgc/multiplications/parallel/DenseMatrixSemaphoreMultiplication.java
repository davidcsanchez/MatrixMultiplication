package es.ulpgc.multiplications.parallel;

import es.ulpgc.Matrix;
import es.ulpgc.MatrixException;
import es.ulpgc.Multiplication;
import es.ulpgc.matrices.DenseMatrix;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class DenseMatrixSemaphoreMultiplication implements Multiplication {

    private static ExecutorService executorService;
    private Semaphore semaphore;
    private double[][] result;

    @Override
    public Matrix execute(Matrix a, Matrix b) {
        checkIsDenseMatrix(a);
        checkIsDenseMatrix(b);
        semaphore = new Semaphore(1);
        executorService = Executors.newFixedThreadPool(a.size());
        result = new double[a.size()][a.size()];
        for (int i = 0; i < a.size(); i++)
            for (int k = 0; k < a.size(); k++)
                for (int j = 0; j < a.size(); j++)
                    submit(a, b, k, i, j);
        try {
            executorService.shutdown();
            executorService.awaitTermination(1000, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
        return new DenseMatrix(result);
    }

    private void submit(Matrix a, Matrix b, int k, int i, int j) {
        executorService.submit(() -> {
            try {
                double value = a.value(i, k) * b.value(k, j);
                semaphore.acquire();
                result[i][j] += value;
                semaphore.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void checkIsDenseMatrix(Matrix matrix) {
        if (matrix instanceof DenseMatrix) return;
        throw new MatrixException("Supplied Matrix is of unsupported type");
    }
}
