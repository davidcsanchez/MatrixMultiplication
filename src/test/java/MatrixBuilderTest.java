import es.ulpgc.Matrix;
import es.ulpgc.MatrixBuilder;
import es.ulpgc.builders.DenseMatrixBuilder;
import es.ulpgc.builders.SparseMatrixBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(Parameterized.class)
public class MatrixBuilderTest {


    private final MatrixBuilder builder;

    public MatrixBuilderTest(MatrixBuilder builder) {
        this.builder = builder;
    }

    @Test
    public void test_builder_values() {
        builder.set(5,5,10d);
        builder.set(0,17,140d);
        builder.set(34,4,30d);
        builder.set(2,55,50d);
        Matrix matrix = builder.build();
        assertThat(matrix.value(5, 5)).isEqualTo(10d);
        assertThat(matrix.value(0, 17)).isEqualTo(140d);
        assertThat(matrix.value(34, 4)).isEqualTo(30d);
        assertThat(matrix.value(2, 55)).isEqualTo(50d);
    }

    @Parameterized.Parameters
    public static Collection<MatrixBuilder> implementations() {
        return List.of(
            new DenseMatrixBuilder(200),
            new SparseMatrixBuilder(200)
        );
    }
}
