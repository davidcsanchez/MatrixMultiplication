package es.ulpgc;

public interface MatrixBuilder {
    Matrix build();
    void set(int row, int col, double val);
    void set(Matrix matrix);
}
