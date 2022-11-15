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

public class SparseMatrixAtomicMultiplication implements Multiplication {

    private static ExecutorService executorService;

    @Override
    public Matrix execute(Matrix a, Matrix b) {
        checkIsSparseMatrix(a);
        checkIsSparseMatrix(b);
        AtomicDouble[][] atomicDoubles = createEmptyAtomicDoubleMatrix(a.size());
        executorService = Executors.newFixedThreadPool(a.size());
        for (int i = 0; i < a.size(); i++)
            for (int k = 0; k < a.size(); k++)
                for (int j = 0; j < a.size(); j++)
                    submit(a, b, atomicDoubles, k, i, j);
        try {
            executorService.shutdown();
            executorService.awaitTermination(1000, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
        return new DenseMatrix(getDoubleValues(atomicDoubles));
    }

    private AtomicDouble[][] createEmptyAtomicDoubleMatrix(int size) {
        AtomicDouble[][] atomicDoubles = new AtomicDouble[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                atomicDoubles[i][j] = new AtomicDouble();
        return atomicDoubles;
    }

    private double[][] getDoubleValues(AtomicDouble[][] atomicDoubles) {
        double[][] result = new double[atomicDoubles.length][atomicDoubles.length];
        for (int i = 0; i < atomicDoubles.length; i++)
            for (int j = 0; j < atomicDoubles.length; j++){
                result[i][j] = atomicDoubles[i][j].value();
            }
        return result;
    }

    private void submit(Matrix a, Matrix b, AtomicDouble[][] atomicDoubles, int k, int i, int j) {
        executorService.submit(() -> {
            if (a.value(i, k) == 0 || b.value(k, j) == 0) return;
            atomicDoubles[i][j].value(a.value(i, k) * b.value(k, j));
        });
    }

    private void checkIsSparseMatrix(Matrix matrix) {
        if (matrix instanceof SparseMatrix) return;
        throw new MatrixException("Supplied Matrix is of unsupported type.");
    }

    private static class AtomicDouble {

        private double value = 0;
        private final Semaphore semaphore = new Semaphore(1);

        public double value() {
            return value;
        }

        public void value(double valueToAdd) {
            try {
                semaphore.acquire();
                value += valueToAdd;
                semaphore.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
