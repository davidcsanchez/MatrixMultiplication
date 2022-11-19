package es.ulpgc.multiplications.parallels;

import es.ulpgc.Matrix;
import es.ulpgc.MatrixException;
import es.ulpgc.Multiplication;
import es.ulpgc.builders.SparseMatrixBuilder;
import es.ulpgc.matrices.DenseMatrix;
import es.ulpgc.matrices.SparseMatrix;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SparseMatrixParallelSynchronizedMultiplication implements Multiplication {
    private static final int MAX_THREADS = Runtime.getRuntime().availableProcessors();

    @Override
    public Matrix execute(Matrix a, Matrix b) {
        checkIsSparseMatrix(a);
        checkIsSparseMatrix(b);
        try {
            ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);
            Context context = new Context(a.size());
            SparseMatrixBuilder builder = new SparseMatrixBuilder(a.size());
            double[][] aValues = a.raw();
            double[][] bValues = b.raw();
            double[][] c = new double[a.size()][a.size()];
            for (int i = 0; i < a.size(); i++) {
                executor.submit(() -> {
                    int row = context.nextRow();
                    for (int j = 0; j < a.size(); j++) {
                        for (int k = 0; k < a.size(); k++) {
                            if (aValues[row][k] == 0 || bValues[k][j] == 0) continue;
                            c[row][j] += aValues[row][k] * bValues[k][j];
                        }
                    }
                });
            }
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.MINUTES);
            builder.set(new DenseMatrix(c));
            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void checkIsSparseMatrix(Matrix matrix) {
        if (matrix instanceof SparseMatrix) return;
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
