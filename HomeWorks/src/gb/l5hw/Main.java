package gb.l5hw;

public class Main {

    public static void main(String[] args) {
        WorkClass workClass = new WorkClass();
        System.out.printf("Один поток: %.2fms%n", workClass.oneThreadSample());
        try
        {
            float[] multiThread = workClass.multiThreadSample();
            System.out.printf("%.0f потоков: %.2fms", multiThread[0], multiThread[1]);
        }
        catch (InterruptedException iex){
            System.out.println(iex.getMessage());
        }
    }
}