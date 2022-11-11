package es.ulpgc.multiplications;

import es.ulpgc.Matrix;
import es.ulpgc.MatrixException;
import es.ulpgc.Multiplication;
import es.ulpgc.matrices.DenseMatrix;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DenseMatrixSemaphoreMultiplication implements Multiplication {

    private static ExecutorService executorService;

        @Override
        public Matrix execute(Matrix a, Matrix b) {
            checkIsDenseMatrix(a);
            checkIsDenseMatrix(b);
            int size = a.size();
            double[][] aValues = a.raw();
            double[][] bValues = b.raw();
            executorService = Executors.newFixedThreadPool(size);
            double[][] result = new double[size][size];
            for (int i = 0; i < size; i++) submit(aValues, bValues, result, size, i);
            try {
                executorService.shutdown();
                executorService.awaitTermination(1000, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
            return new DenseMatrix(result);
        }

        private void submit(double[][] a, double[][] b, double[][] c, int size, int i) {
            executorService.submit(() -> {
                for (int k = 0; k < size; k++)
                    for (int j = 0; j < size; j++)
                        c[i][j] += a[i][k] * b[k][j];
            });
        }

        private void checkIsDenseMatrix(Matrix matrix) {
            if (matrix instanceof DenseMatrix) return;
            throw new MatrixException("Supplied Matrix is of unsupported type");
        }
}
