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
        a = Matrix.create(a.raw());
        b = Matrix.create(b.raw());
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
                if (a.value(i, k) == 0 || b.value(k, j) == 0) return;
                double value = a.value(i, k) * b.value(k, j);
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
