package es.ulpgc.matrices;

import es.ulpgc.Matrix;

public class CcsMatrix implements Matrix {
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
}
