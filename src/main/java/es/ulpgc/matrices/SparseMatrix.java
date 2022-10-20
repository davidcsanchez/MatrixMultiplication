package es.ulpgc.matrices;

import es.ulpgc.Matrix;

import java.util.List;

public class SparseMatrix implements Matrix {

    private List<Coordinate> list;

    @Override
    public double value(int row, int col) {
        return 0;
    }

    @Override
    public double[][] raw() {
        return new double[0][];
    }

    @Override
    public int size() {
        return 0;
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
            return (newRow == row && newRow == col);
        }

        public double value() {
            return value;
        }
    }
}
