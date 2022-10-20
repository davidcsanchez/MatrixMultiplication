import es.ulpgc.Matrix;
import es.ulpgc.builders.SparseMatrixBuilder;
import es.ulpgc.matrices.CcsMatrix;
import es.ulpgc.matrices.CrsMatrix;
import es.ulpgc.matrices.DenseMatrix;
import es.ulpgc.matrices.SparseMatrix;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.List;
import static org.assertj.core.api.Assertions.*;

@RunWith(Parameterized.class)
public class MatrixTest {

    private final Matrix matrix;

    public MatrixTest(Matrix matrix) {
        this.matrix = matrix;
    }

    @Test
    public void test_matrix_all_values() {
        assertThat(getSimpleMatrix()).isEqualTo(matrix.raw());
    }

    @Test
    public void test_matrix_values() {
        assertThat(5d).isEqualTo(matrix.value(0,0));
        assertThat(8d).isEqualTo(matrix.value(1,1));
        assertThat(3d).isEqualTo(matrix.value(2,2));
        assertThat(6d).isEqualTo(matrix.value(3,1));
    }

    @Test
    public void test_matrix_size() {
        assertThat(matrix.size()).isEqualTo(4);
    }

    @Test
    public void test_matrix_density() {
        assertThat(matrix.density()).isEqualTo((double) 4/16);
    }

    @Parameterized.Parameters
    public static Collection<Matrix> implementations() {
        return List.of(new DenseMatrix(getSimpleMatrix()),
            getCrsMatrixForTesting(),
            getCcsMatrixForTesting(),
            getSparseMatrixForTesting()
        );
    }

    private static SparseMatrix getSparseMatrixForTesting() {
        SparseMatrixBuilder sparseMatrixBuilder = new SparseMatrixBuilder(4);
        sparseMatrixBuilder.set(new DenseMatrix(getSimpleMatrix()));
        return (SparseMatrix) sparseMatrixBuilder.build();
    }

    private static CrsMatrix getCrsMatrixForTesting() {
        int[] rowPointers = new int[]{0,1,2,3,4};
        int[] columns = new int[]{0,1,2,1};
        double[] values = new double[]{5,8,3,6};
        return new CrsMatrix(rowPointers, columns, values);
    }

    private static CcsMatrix getCcsMatrixForTesting() {
        int[] rows = new int[]{0,1,3,2};
        int[] columnPointers = new int[]{0,1,3,4,4};
        double[] values = new double[]{5,8,6,3};
        return new CcsMatrix(rows, columnPointers, values);
    }

    private static double[][] getSimpleMatrix() {
        double[][] values = new double[4][4];
        values[0][0] = 5;
        values[1][1] = 8;
        values[2][2] = 3;
        values[3][1] = 6;
        return values;
    }
}
