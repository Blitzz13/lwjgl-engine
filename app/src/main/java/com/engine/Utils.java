package com.engine;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class Utils {

    private Utils() {
        // Utility class
    }

    public static String readFile(String resourcePath) {
        // Use the ClassLoader to find the file inside src/main/resources
        try (InputStream in = Utils.class.getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new RuntimeException("Could not find resource: " + resourcePath);
            }
            // Use a Scanner to read the entire stream into a String
            try (Scanner scanner = new Scanner(in, StandardCharsets.UTF_8.name())) {
                return scanner.useDelimiter("\\A").next();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error reading resource [" + resourcePath + "]", e);
        }
    }

    public static float[] listFloatToArray(List<Float> list) {
        int size = list != null ? list.size() : 0;
        float[] floatArr = new float[size];
        for (int i = 0; i < size; i++) {
            floatArr[i] = list.get(i);
        }
        return floatArr;
    }

    public static int[] listIntToArray(List<Integer> list) {
        return list.stream().mapToInt((Integer v) -> v).toArray();
    }
}