import es.ulpgc.Matrix;
import es.ulpgc.Multiplication;
import es.ulpgc.matrices.DenseMatrix;
import es.ulpgc.multiplications.DenseMatrixParallelStreamMultiplication;
import es.ulpgc.multiplications.DenseMatrixRowMultiplication;
import es.ulpgc.multiplications.DenseMatrixStandardMultiplication;
import es.ulpgc.multiplications.DenseMatrixTransposedMultiplication;
import es.ulpgc.transposers.DenseMatrixTransposer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.*;

@RunWith(Parameterized.class)
public class DenseMatrixMultiplicationTest {

    private final int SIZE = 1024;

    private final Multiplication multiplication;

    public DenseMatrixMultiplicationTest(Multiplication multiplication) {
        this.multiplication = multiplication;
    }

    @Test
    public void should_multiply_two_random_double_matrices() {
        Matrix a = randomMatrix();
        Matrix b = randomMatrix();
        Matrix c = multiplication.execute(a,b);
        Vector vector = new Vector(SIZE);
        assertThat(vector.multiply(c)).isEqualTo(vector.multiply(b).multiply(a));
    }

    private Matrix randomMatrix() {
        Random random = new Random();
        double[][] values = new double[SIZE][SIZE];

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                values[i][j] = random.nextDouble();
            }
        }
        return new DenseMatrix(values);
    }

    @Parameterized.Parameters
    public static Collection<Multiplication> implementations() {
        return List.of(
                new DenseMatrixStandardMultiplication(),
                new DenseMatrixRowMultiplication(),
                new DenseMatrixTransposedMultiplication(new DenseMatrixTransposer()),
                new DenseMatrixParallelStreamMultiplication()
        );
    }

    static class Vector {
        private static final double EPSILON = 1E-5;
        private final double[] values;

        public Vector(int size) {
            this.values = randomVector(size);
        }

        private Vector(double[] values) {
            this.values = values;
        }


        private double[] randomVector(int size) {
            Random random = new Random();
            double[] result = new double[size];
            for (int i = 0; i < size; i++)
                result[i] = random.nextDouble();
            return result;
        }

        public Vector multiply(Matrix matrix) {
            double[] result = new double[values.length];
            for (int i = 0; i < matrix.size(); i++) {
                for (int j = 0; j < result.length; j++) {
                    result[i] += matrix.value(i, j) * values[j];
                }
            }
            return new Vector(result);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Vector vector = (Vector) o;
            for (int i = 0; i < values.length; i++) {
                if (Math.abs(values[i] - vector.values[i]) > EPSILON) return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(values);
        }
    }
}