package es.ulpgc.matrices;

import es.ulpgc.Matrix;

import java.util.Arrays;

public class DenseMatrix implements Matrix {

    private final double[][] values;

    public DenseMatrix(double[][] values) {
        this.values = values;
    }

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
}
