package es.ulpgc.multiplications.parallels;

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
    private Semaphore[][] semaphores;
    private static final int MAX_THREADS = Runtime.getRuntime().availableProcessors();
    private double[][] result;

    @Override
    public Matrix execute(Matrix a, Matrix b) {
        checkIsDenseMatrix(a);
        checkIsDenseMatrix(b);
        createSemaphores(a.size());
        executorService = Executors.newFixedThreadPool(MAX_THREADS);
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

    private void createSemaphores(int size) {
        semaphores = new Semaphore[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                semaphores[i][j] = new Semaphore(1);
    }

    private void submit(Matrix a, Matrix b, int k, int i, int j) {
        executorService.submit(() -> {
            try {
                double value = a.value(i, k) * b.value(k, j);
                semaphores[i][j].acquire();
                result[i][j] += value;
                semaphores[i][j].release();
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
