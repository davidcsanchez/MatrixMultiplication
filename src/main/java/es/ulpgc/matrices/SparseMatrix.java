package es.ulpgc.matrices;

import es.ulpgc.Matrix;

import java.util.List;

public class SparseMatrix implements Matrix {

    private final List<Coordinate> values;
    private final int size;

    public SparseMatrix(List<Coordinate> values, int size) {
        this.values = values;
        this.size = size;
    }

    @Override
    public double value(int row, int col) {
        if (row > size() || col > size()) throw new RuntimeException("Exceeded the matrix length");
        return values.stream().filter(coordinate -> coordinate.checkSameCoords(row, col))
            .findFirst().stream().mapToDouble(coordinate -> coordinate.value).findFirst().orElse(0);
    }

    @Override
    public double[][] raw() {
        return new double[0][];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public double density() {
        return 0;
    }

    private static class Coordinate {

        private final int row;
        private final int col;
        private final double value;

        public Coordinate(int row, int col, double value) {
            this.row = row;
            this.col = col;
            this.value = value;
        }

        public boolean checkSameCoords(int newRow, int newCol) {
            return (newRow == row && newCol == col);
        }

        public double value() {
            return value;
        }
    }
}
