package benchmarkings;

import es.ulpgc.Matrix;
import es.ulpgc.Multiplication;
import es.ulpgc.builders.SparseMatrixBuilder;
import es.ulpgc.multiplications.parallels.SparseMatrixParallelMultiplication;
import es.ulpgc.multiplications.parallels.SparseMatrixParallelSynchronizedMultiplication;
import es.ulpgc.multiplications.parallels.SparseMatrixSemaphoreMultiplication;
import es.ulpgc.multiplications.parallels.SparseMatrixThreadPoolMultiplication;
import org.openjdk.jmh.annotations.*;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@BenchmarkMode(Mode.AverageTime)
@Fork(value = 2)
@Warmup(iterations = 2, time = 2)
@Measurement(iterations = 3, time = 2)
public class SparseMatrixMultiplicationBenchmark {
    private static final int SIZE = 100;
    private static final Random random = new Random();

    @Benchmark
    public static void SparseMatrixThreadPoolMultiplication() {
        executeWith(new SparseMatrixThreadPoolMultiplication());
    }

    @Benchmark
    public static void SparseMatrixParallelMultiplication() {executeWith(new SparseMatrixParallelMultiplication());}

    @Benchmark
    public static void SparseMatrixParallelSynchronizedMultiplication() {executeWith(new SparseMatrixParallelSynchronizedMultiplication());}

    @Benchmark
    public static void SparseMatrixSemaphoreMultiplication() {executeWith(new SparseMatrixSemaphoreMultiplication());}

    private static void executeWith(Multiplication implementation) {
        implementation.execute(randomSparseMatrix(), randomSparseMatrix());
    }

    private static Matrix randomSparseMatrix() {
        SparseMatrixBuilder builder = new SparseMatrixBuilder(SIZE);
        for (int i = 0; i < SIZE; i++) {
            Set<Integer> positions = RandomPositions();
            for (Integer position : positions) builder.set(position, i, random.nextDouble());
        }
        return builder.build();
    }

    private static Set<Integer> RandomPositions() {
        Set<Integer> result = new HashSet<>();
        int nonzeros = SparseMatrixMultiplicationBenchmark.random.ints(0, SIZE / 2)
                .findFirst()
                .getAsInt();
        for (int i = 0; i < nonzeros; i++) {
            result.add(SparseMatrixMultiplicationBenchmark.random.ints(0, SIZE)
                    .findFirst()
                    .getAsInt());
        }
        return result;
    }
}
