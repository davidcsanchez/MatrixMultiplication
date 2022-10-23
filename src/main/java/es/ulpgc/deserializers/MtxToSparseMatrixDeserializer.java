package es.ulpgc.deserializers;

import es.ulpgc.MatrixDeserializer;
import es.ulpgc.matrices.SparseMatrix;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MtxToSparseMatrixDeserializer implements MatrixDeserializer {

    @Override
    public SparseMatrix deserialize(String filename) {
        InputStream is = MtxToSparseMatrixDeserializer.getInputStream(filename);
        BufferedReader reader = MtxToSparseMatrixDeserializer.getBufferedReader(is);
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

    private SparseMatrix readFile(BufferedReader reader) {
        int size = reader.lines().filter(line -> !line.startsWith("%"))
            .map(this::getSize).findFirst().orElse(0);
        return new SparseMatrix(reader.lines().map(line -> saveValues(getLineValues(line))).collect(Collectors.toList()), size);
    }

    private int getSize(String line) {
        return Arrays.stream(line.split(" ")).mapToInt(Integer::parseInt).toArray()[0];
    }

    private SparseMatrix.Coordinate saveValues(double[] lineValues) {
        return new SparseMatrix.Coordinate((int) lineValues[0] - 1, (int) lineValues[1] - 1, lineValues[2]);
    }

    private double[] getLineValues(String line) {
        return Arrays.stream(line.split(" ")).mapToDouble(Double::parseDouble).toArray();
    }
}
