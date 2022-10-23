package es.ulpgc;

public interface MatrixTransposer extends UnaryOperator {
    @Override
    Matrix execute(Matrix matrix);
}
