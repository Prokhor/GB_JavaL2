package gb.l2hw;

import java.util.Random;

public class Main {

    private static Random rand;

    public static void main(String[] args) {

        String[][] testArrayGood = initTestArray(4, 4, false);
        printArray(testArrayGood);
        printDuoArray(testArrayGood);
        System.out.println("*******************************************");
        String[][] testArrayBadDimension1 = initTestArray(3, 4, true);
        printArray(testArrayBadDimension1);
        try{
            printDuoArray(testArrayBadDimension1);
        } catch (MyArraySizeException e){
            System.out.println(e.getMessage());
        }
        System.out.println("*******************************************");
        String[][] testArrayBadDimension2 = initTestArray(4, 3, true);
        printArray(testArrayBadDimension2);
        try{
            printDuoArray(testArrayBadDimension2);
        } catch (MyArraySizeException e){
            System.out.println(e.getMessage());
        }
        System.out.println("*******************************************");
        String[][] testArrayBadDimension3 = initTestArray(4, 5, true);
        printArray(testArrayBadDimension3);
        try{
            printDuoArray(testArrayBadDimension3);
        } catch (MyArraySizeException e){
            System.out.println(e.getMessage());
        }
        System.out.println("*******************************************");
        String[][] testArrayBadData1 = initTestArray(4, 4, true);
        printArray(testArrayBadData1);
        printDuoArray(testArrayBadData1);
        System.out.println("*******************************************");
        String[][] testArrayBadData2 = initTestArray(4, 4, true);
        printArray(testArrayBadData2);
        printDuoArray(testArrayBadData2);
    }

    private static void printDuoArray(String[][] input) throws MyArrayDataException, MyArraySizeException {
        // специально сделал для того, чтобы неправильный по размеру сразу выкидывал
        //try{
        checkArrayDimension(input);
//        } catch (MyArraySizeException e){
//            System.out.println(e.getMessage());
//        }
        int sumArray = 0;
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[i].length; j++) {
                try {
                    sumArray += tryParseInt(input[i][j], i, j);
                } catch (MyArrayDataException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        System.out.printf("Сумма элементов в массиве: %d\n", sumArray);
    }

    private static void printArray(String[][] array) {
        for (String[] strings : array) {
            for (String string : strings) {
                System.out.print(string + " ");
            }
            System.out.println();
        }
    }

    private static void checkArrayDimension(String[][] array) throws MyArraySizeException {
        if (array.length != 4) {
            throw new MyArraySizeException("Число строк в массиве должно быть равно 4: " + array.length, array.length);
        }
        for (String[] strings : array) {
            if (strings.length != 4) {
                throw new MyArraySizeException("Число столбцов в массиве должно быть равно 4: " + strings.length, strings.length);
            }
        }
    }

    private static int tryParseInt(String input, int row, int col) throws MyArrayDataException {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new MyArrayDataException(String.format("Не удалось привести данные к типу Integer: %s [строка %d столбец %d]%n", input, row + 1, col + 1), row, col);
        }
    }

    private static String[][] initTestArray(int rows, int cols, boolean badData) {
        rand = new Random();

        String[][] testArray = new String[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                testArray[i][j] = String.valueOf(rand.nextInt(9) + 1);
            }
        }

        if (badData) {
            for (int i = 0; i < cols; i++) {
                testArray[rand.nextInt(rows)][rand.nextInt(cols)] = Character.toString((char) (rand.nextInt(90) + 33));
            }
        }

        return testArray;
    }
}
