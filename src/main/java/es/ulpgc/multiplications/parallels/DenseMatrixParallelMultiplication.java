package es.ulpgc.multiplications.parallels;

import es.ulpgc.Matrix;
import es.ulpgc.MatrixException;
import es.ulpgc.Multiplication;
import es.ulpgc.matrices.DenseMatrix;

import java.util.ArrayList;
import java.util.List;

public class DenseMatrixParallelMultiplication implements Multiplication {
    private static final int MAX_THREADS = Runtime.getRuntime().availableProcessors();
    List<Thread> threads = new ArrayList<>(MAX_THREADS);

    @Override
    public Matrix execute(Matrix a, Matrix b) {
        try {
            checkIsDenseMatrix(a);
            checkIsDenseMatrix(b);
            double[][] c = new double[a.size()][a.size()];
            for (int i = 0; i < a.size(); i++) {
                RowMultiplicationTask task = new RowMultiplicationTask(a, b, i, c);
                Thread thread = new Thread(task);
                thread.start();
                threads.add(thread);
                if (threads.size() == MAX_THREADS) {
                    waitForThreads();
                }
            }
            waitForThreads();
            return new DenseMatrix(c);
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

    private void checkIsDenseMatrix(Matrix matrix) {
        if (matrix instanceof DenseMatrix) return;
        throw new MatrixException("Supplied Matrix is of unsupported type");
    }

    private static class RowMultiplicationTask implements Runnable {
        private final Matrix a;
        private final Matrix b;
        private final int row;
        private final double[][] c;


        public RowMultiplicationTask(Matrix a, Matrix b, int row, double[][] c) {
            this.a = a;
            this.b = b;
            this.row = row;
            this.c = c;
        }

        @Override
        public void run() {
            for (int i = 0; i < a.size(); i++)
                for (int j = 0; j < a.size(); j++)
                    c[row][i] += a.value(row, j) * b.value(j, i);
        }
    }
}
