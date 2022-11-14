package es.ulpgc.multiplications;

import es.ulpgc.Matrix;
import es.ulpgc.MatrixBuilder;
import es.ulpgc.MatrixException;
import es.ulpgc.Multiplication;
import es.ulpgc.builders.SparseMatrixBuilder;
import es.ulpgc.matrices.SparseMatrix;

import java.util.ArrayList;
import java.util.List;

public class SparseMatrixParallelMultiplication implements Multiplication {

    private static final int MAX_THREADS = Runtime.getRuntime().availableProcessors();
    List<Thread> threads = new ArrayList<>(MAX_THREADS);

    @Override
    public Matrix execute(Matrix a, Matrix b) {
        try {
            checkIsSparseMatrix(a);
            checkIsSparseMatrix(b);
            SparseMatrixBuilder builder = new SparseMatrixBuilder(a.size());
            for (int i = 0; i < a.size(); i++) {
                RowMultiplicationTask task = new RowMultiplicationTask(a.raw(), b.raw(), i, builder);
                Thread thread = new Thread(task);
                thread.start();
                threads.add(thread);
                if (threads.size() == MAX_THREADS) {
                    waitForThreads();
                }
            }
            waitForThreads();
            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void waitForThreads() throws InterruptedException {
        for (Thread thread : threads) {
            thread.join();
        }
        threads.clear();
    }

    private void checkIsSparseMatrix(Matrix matrix) {
        if (matrix instanceof SparseMatrix) return;
        throw new MatrixException("Supplied Matrix is of unsupported type");
    }

    private static class RowMultiplicationTask implements Runnable {
        private final double[][] a;
        private final double[][] b;
        private final int row;
        private final MatrixBuilder builder;

        public RowMultiplicationTask(double[][] a, double[][] b, int row, SparseMatrixBuilder builder) {
            this.a = a;
            this.b = b;
            this.row = row;
            this.builder = builder;
        }

        @Override
        public void run() {
            double sum;
            for (int i = 0; i < a[0].length; i++) {
                sum = 0;
                for (int j = 0; j < a[0].length; j++) {
                    if (a[row][j] == 0 || b[j][i] == 0) continue;
                    sum += a[row][j] * b[j][i];
                }
                builder.set(row, i, sum);
            }
        }
    }
}
