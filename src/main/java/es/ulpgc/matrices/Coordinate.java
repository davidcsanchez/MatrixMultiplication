package es.ulpgc.matrices;

public class Coordinate implements Comparable<Coordinate> {

    public final int row;
    public final int col;
    public final double value;

    public Coordinate(int row, int col, double value) {
        this.row = row;
        this.col = col;
        this.value = value;
    }

    public boolean checkEquals(int newRow, int newCol) {
        return (newRow == row && newCol == col);
    }

    @Override
    public int compareTo(Coordinate o) {
        return this.row - o.row;
    }
}