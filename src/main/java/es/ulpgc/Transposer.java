package es.ulpgc;

public interface Transposer extends UnaryOperator {
    @Override
    Matrix execute(Matrix matrix);
}
