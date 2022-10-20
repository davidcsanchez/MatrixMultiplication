package es.ulpgc.matrices;

import es.ulpgc.Matrix;

import java.util.stream.IntStream;

public class CcsMatrix implements Matrix {

    public final int[] rows;
    public final int[] columnPointers;
    public final double[] values;

    public CcsMatrix(int[] rows, int[] columnPointers, double[] values) {
        this.rows = rows;
        this.columnPointers = columnPointers;
        this.values = values;
    }

    @Override
    public double value(int row, int col) {
        if (row > size() || col > size()) throw new RuntimeException("Exceeded the matrix length");
        return IntStream.range(columnPointers[col], columnPointers[col + 1])
            .filter(currentValueId -> rows[currentValueId] == row)
            .mapToDouble(currentValueId -> values[currentValueId]).findFirst().orElse(0d);
    }

    @Override
    public double[][] raw() {
        int size = size();
        double[][] matrix = new double[size][size];
        IntStream.range(0, size).forEach(colId -> fillCol(matrix, colId));
        return matrix;
    }

    private void fillCol(double[][] matrix, int colId) {
        IntStream.range(columnPointers[colId], columnPointers[colId + 1]).forEach(index ->
            matrix[rows[index]][colId] = values[index]);
    }

    @Override
    public int size() {
        return columnPointers.length - 1;
    }

    @Override
    public double density() {
        return (double) values.length / Math.pow(columnPointers.length - 1, 2);
    }
}
