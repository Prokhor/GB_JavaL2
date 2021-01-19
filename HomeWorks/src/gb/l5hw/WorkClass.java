package gb.l5hw;

import java.util.*;

public class WorkClass {

    private final int ARRAY_SIZE = 10000000;

    private float[] initArray() {
        float[] result = new float[ARRAY_SIZE];
        Arrays.fill(result, 1);
        return result;
    }

    private float calculate(float num, int index) {
        return (float) (num * Math.sin(0.2f + (float) (index / 5)) * Math.cos(0.2f + (float) (index / 5)) * Math.cos(0.4f + (float) (index / 2)));
    }

    public float oneThreadSample() {
        float[] arr = initArray();
        long start = System.currentTimeMillis();
        for (int i = 0; i < ARRAY_SIZE; i++) {
            arr[i] = calculate(arr[i], i);
        }
        return System.currentTimeMillis() - start;
    }

    public float[] multiThreadSample() throws InterruptedException {
        float[] result = new float[2];
        Random rand = new Random();
        int threadsCnt = rand.nextInt(51) + 2;
        while (ARRAY_SIZE % threadsCnt != 0) {
            threadsCnt = rand.nextInt(51) + 2;
        }
        result[0] = threadsCnt;

        float[] arr = initArray();
        float[] resultArr = new float[ARRAY_SIZE];
        long start = System.currentTimeMillis();
        List<float[]> arrayParts = new ArrayList<>();
        Thread[] arrayThreads = new Thread[threadsCnt];
        int elements = ARRAY_SIZE / threadsCnt;
        for (int i = 1; i <= threadsCnt; i++) {
            arrayParts.add(Arrays.copyOfRange(arr, (i - 1) * elements, i * elements));
        }
        for (int i = 0; i < threadsCnt; i++) {
            int threadI = i;
            arrayThreads[i] = new Thread(() -> {
                for (int j = 0; j < elements; j++) {
                    arrayParts.get(threadI)[j] = calculate(arrayParts.get(threadI)[j], j);
                }
            });
            arrayThreads[i].start();
            arrayThreads[i].join();
        }
        for (int i = 0; i < threadsCnt; i++) {
            System.arraycopy(arrayParts.get(i), 0, resultArr, i * elements, elements);
        }
        result[1] = System.currentTimeMillis() - start;

        return result;
    }
}