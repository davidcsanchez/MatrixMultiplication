package es.ulpgc.multiplications;

import es.ulpgc.Matrix;
import es.ulpgc.MatrixException;
import es.ulpgc.Multiplication;
import es.ulpgc.builders.SparseMatrixBuilder;
import es.ulpgc.matrices.CcsMatrix;
import es.ulpgc.matrices.CrsMatrix;

public class CompressedSparseMatrixMultiplication implements Multiplication {
    @Override
    public Matrix execute(Matrix a, Matrix b) {
        checkisCrs(a);
        checkisCcs(b);
        CrsMatrix A = (CrsMatrix) a;
        CcsMatrix B = (CcsMatrix) b;
        int size = a.size();
        SparseMatrixBuilder builder = new SparseMatrixBuilder(size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int ii = A.rowPointers[i];
                int iEnd = A.rowPointers[i+1];
                int jj = B.colPointers[j];
                int jEnd = B.colPointers[j+1];
                long s = 0;
                while (ii < iEnd && jj < jEnd) {
                    int aa = A.columns[ii];
                    int bb = B.rows[jj];
                    if (aa == bb) {
                        s += A.values[ii] * B.values[jj];
                        ii++;
                        jj++;
                    }
                    else if (aa < bb) ii++;
                    else jj++;
                }
                if (s != 0) builder.set(i, j, s);
            }
        }
        return builder.build();
    }

    private void checkisCrs(Matrix matrix) {
        if (matrix instanceof CrsMatrix) return;
        throw new MatrixException("Supplied Matrix is of unsupported type");
    }

    private void checkisCcs(Matrix matrix) {
        if (matrix instanceof CcsMatrix) return;
        throw new MatrixException("Supplied Matrix is of unsupported type");
    }
}