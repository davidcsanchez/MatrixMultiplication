import es.ulpgc.Matrix;
import es.ulpgc.builders.SparseMatrixBuilder;
import es.ulpgc.matrices.SparseMatrix;
import es.ulpgc.multiplications.SparseMatrixStandardMultiplication;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.*;

public class SparseMatrixMultiplicationTest {
    private static final int SIZE = 50;

    @Test
    public void should_multiply_two_random_double_matrices() {
        Matrix a = sparseRandomMatrix();
        Matrix b = sparseRandomMatrix();
        SparseMatrixStandardMultiplication multiplication = new SparseMatrixStandardMultiplication();
        Matrix c = multiplication.execute(a,b);
        Vector vector = new Vector(SIZE);
        assertThat(vector.multiply(c)).isEqualTo(vector.multiply(b).multiply(a));
    }

    private static SparseMatrix sparseRandomMatrix() {
        Random random = new Random();
        SparseMatrixBuilder builder = new SparseMatrixBuilder(SIZE);
        for (int column = 0; column < SIZE; column++) {
            List<Integer> randomPositions = getRandomPositions(random);
            for (int row : randomPositions) {
                builder.set(row, column ,random.nextDouble());
            }
        }
        return (SparseMatrix) builder.build();
    }

    private static List<Integer> getRandomPositions(Random random) {
        List<Integer> result = new ArrayList<>();
        int amountToAdd = random.ints(0, SIZE / 2)
                .findFirst()
                .getAsInt();
        for (int i = 0; i < amountToAdd; i++) {
            result.add(random.ints(0, SIZE)
                    .findFirst()
                    .getAsInt());
        }
        return result;
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
