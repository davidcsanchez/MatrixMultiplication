package es.ulpgc;

public interface Multiplication extends BinaryOperator {
    @Override
    Matrix execute(Matrix a, Matrix b);
}
