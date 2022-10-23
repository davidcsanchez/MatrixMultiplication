package es.ulpgc;

public interface Matrix {
    double value(int row, int col);
    double[][] raw();
    int size();
    double density();
    default double sparsity() {
        return 1 - density();
    }
}
