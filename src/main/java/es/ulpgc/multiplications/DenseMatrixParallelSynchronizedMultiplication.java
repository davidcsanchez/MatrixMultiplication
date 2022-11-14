package es.ulpgc.multiplications;

import es.ulpgc.Matrix;
import es.ulpgc.MatrixException;
import es.ulpgc.Multiplication;
import es.ulpgc.matrices.DenseMatrix;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DenseMatrixParallelSynchronizedMultiplication implements Multiplication {
    private static final int MAX_THREADS = Runtime.getRuntime().availableProcessors();

    @Override
    public Matrix execute(Matrix a, Matrix b) {
        checkIsDenseMatrix(a);
        checkIsDenseMatrix(b);
        try {
                ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);
                Context context = new Context(a.size());
                double[][] c = new double[a.size()][a.size()];
                for (int i = 0; i < a.size(); i++) {
                    executor.submit(() -> {
                    int row = context.nextRow();
                    for (int j = 0; j < a.size(); j++) {
                        for (int k = 0; k < a.size(); k++) {
                            c[row][j] += a.value(row, k) * b.value(k, j);
                        }
                    }
                });
            }
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.MINUTES);
            return new DenseMatrix(c);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void checkIsDenseMatrix(Matrix matrix) {
        if (matrix instanceof DenseMatrix) return;
        throw new MatrixException("Supplied Matrix is of unsupported type");
    }

    private static class Context {
        private final int rowCount;
        private int nextRow;

        public Context(int rowCount) {
            this.rowCount = rowCount;
            this.nextRow = 0;
        }

        public synchronized int nextRow() {
            if (isFullyProcessed()) {
                throw new IllegalStateException("Already fully processed");
            }
            return nextRow++;
        }

        public synchronized boolean isFullyProcessed() {
            return nextRow == rowCount;
        }
    }
}
