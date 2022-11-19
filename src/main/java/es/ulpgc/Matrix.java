package es.ulpgc;

import java.util.Arrays;

public interface Matrix {
    double value(int row, int col);
    double[][] raw();
    int size();
    double density();
    default double sparsity() {
        return 1 - density();
    }

    static Matrix create(double[][] values) {
        return new Matrix() {
            @Override
            public double value(int row, int col) {
                return values[row][col];
            }

            @Override
            public double[][] raw() {
                return values;
            }

            @Override
            public int size() {
                return values.length;
            }

            @Override
            public double density() {
                double nonZeroValues = Math.toIntExact(Arrays.stream(values)
                    .flatMapToDouble(Arrays::stream)
                    .filter(value -> value != 0d)
                    .count());
                return nonZeroValues / (Math.pow(values.length, 2));
            }
        };
    }
}
