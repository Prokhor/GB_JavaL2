package gb.l1hw;

public class Robot implements Functionalable, Informable {
    private String name;
    private double maxHeight;
    private int currentCharge;
    private boolean batteryLow;

    public Robot(String name, double maxHeight) {
        this(name, maxHeight, 100);
    }

    public Robot(String name, double maxHeight, int currentCharge) {
        this.name = name;
        this.maxHeight = maxHeight;
        this.currentCharge = currentCharge;
    }

    public boolean isCanContinue() {
        return !batteryLow;
    }

    public String getName() {
        return name;
    }

    @Override
    public void run(int distance) {
        if (!batteryLow) {
            for (int i = 0; i < distance / 100; i++) {
                currentCharge -= 1;
            }
            if (currentCharge > 0) {
                System.out.printf("Робот %s пробежал %d\nЗаряда осталось %d\n", name, distance, currentCharge);
                batteryLow = false;
            } else {
                System.out.printf("Робот %s полностью разряжен.\n", name);
                batteryLow = true;
            }
        }
    }

    @Override
    public void jump(double height) {
        if (!batteryLow) {
            if (maxHeight >= height) {
                for (int i = 0; i <= height; i++) {
                    currentCharge -= 10;
                }
                if (currentCharge > 0) {
                    System.out.printf("Робот %s запрыгнул на стену %.2fм\nЗаряда осталось %d\n", name, height, currentCharge);
                    batteryLow = false;
                } else {
                    System.out.printf("Роботу %s не хватило заряда, чтобы допрыгнуть. Робот разряжен\n", name);
                    batteryLow = true;
                }
            } else {
                batteryLow = true;
            }
        }
    }
}
