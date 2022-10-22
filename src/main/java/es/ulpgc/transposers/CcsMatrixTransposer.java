package es.ulpgc.transposers;

import es.ulpgc.Matrix;
import es.ulpgc.MatrixTransposer;
import es.ulpgc.matrices.CcsMatrix;
import es.ulpgc.matrices.CrsMatrix;

public class CcsMatrixTransposer implements MatrixTransposer {

    @Override
    public Matrix execute(Matrix matrix) {
        CcsMatrix a = (CcsMatrix) matrix;
        return new CrsMatrix(a.colPointers, a.rows, a.values);
    }
}
