package gb.l1hw;

public class Human implements Functionalable, Informable {

    private String name;
    private int maxAbility;
    private double maxHeight;
    private boolean canContinue;
    private int currentDistance;

    public Human(String name, int maxAbility, double maxHeight) {
        this.name = name;
        this.maxAbility = maxAbility;
        this.maxHeight = maxHeight;
        canContinue = true;
        currentDistance = 0;
    }

    public boolean isCanContinue() {
        return canContinue;
    }

    public String getName() {
        return name;
    }

    @Override
    public void run(int distance) {
        if (canContinue) {
            if (maxAbility - currentDistance >= distance) {
                currentDistance += distance;
                System.out.printf("%s пробежал %dм по дорожке!\nВсего %s пробежал %dм!\n", name, distance, name, currentDistance);
                canContinue = true;
            } else {
                System.out.printf("%s не добежал %dм и сошёл с дистанции...\n", name, distance - maxAbility + currentDistance);
                canContinue = false;
            }
        }
    }

    @Override
    public void jump(double height) {
        if (canContinue) {
            if (maxHeight >= height) {
                System.out.printf("%s запрыгнул на стену: %.2fм!\n", name, height);
                canContinue = true;
            } else {
                System.out.printf("%s не смог запрыгнуть на %.2fм и сошёл с дистанции...\n", name, height);
                canContinue = false;
            }
        }
    }
}
