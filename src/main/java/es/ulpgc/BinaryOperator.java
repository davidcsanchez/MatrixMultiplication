package es.ulpgc;

public interface BinaryOperator extends Operator{
    Matrix execute(Matrix a, Matrix b);
}
