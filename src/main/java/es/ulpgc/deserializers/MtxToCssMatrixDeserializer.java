package es.ulpgc.deserializers;

import es.ulpgc.MatrixDeserializer;
import es.ulpgc.matrices.CcsMatrix;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class MtxToCssMatrixDeserializer implements MatrixDeserializer {
    private int[] rows;
    private double[] values;
    private int[] columnPointers;
    private int previousColumn = 0;
    private int size;
    private int nonZeroValues;

    @Override
    public CcsMatrix deserialize(String filename) {
        InputStream is = MtxToCssMatrixDeserializer.getInputStream(filename);
        BufferedReader reader = MtxToCssMatrixDeserializer.getBufferedReader(is);
        return readFile(reader);
    }

    private static BufferedReader getBufferedReader(InputStream is) {
        InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
        return new BufferedReader(streamReader);
    }

    private static InputStream getInputStream(String filename) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        return classloader.getResourceAsStream(filename);
    }

    private CcsMatrix readFile(BufferedReader reader) {
        reader.lines().filter(line -> !line.startsWith("%")).limit(1)
                .forEach(this::readFirstLine);
        reader.lines().forEach(line -> saveValues(getLineValues(line), previousColumn));
        fillLastPointers(previousColumn);
        return new CcsMatrix(
                rows,
                columnPointers,
                values
        );
    }

    private void fillLastPointers(int previousColumn) {
        for (int i = previousColumn + 1; i < size + 1; i++) {
            columnPointers[i] = this.values.length;
        }
    }

    private void readFirstLine(String line) {
        int[] firstLine = Arrays.stream(line.split(" ")).mapToInt(Integer::parseInt).toArray();
        values = new double[firstLine[2]];
        rows = new int[firstLine[2]];
        size = firstLine[0];
        columnPointers = new int[size + 1];
    }

    private void saveValues(double[] lineValues, int previousColumn) {
        if ((lineValues[1] - 1) != previousColumn) this.columnPointers[(int) (lineValues[1] - 1)] = nonZeroValues;
        this.rows[nonZeroValues] = ((int) lineValues[0] - 1);
        this.values[nonZeroValues] = lineValues[2];
        this.previousColumn = (int) lineValues[1] - 1;
        nonZeroValues++;
    }

    private double[] getLineValues(String line) {
        return Arrays.stream(line.split(" ")).mapToDouble(Double::parseDouble).toArray();
    }
}


