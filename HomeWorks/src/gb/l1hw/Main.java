package gb.l1hw;

public class Main {

    public static void main(String[] args) {
        Actionable[] obstacles = new Actionable[10];

        obstacles[0] = new Track(500);
        obstacles[1] = new Track(1500);
        obstacles[2] = new Track(200);
        obstacles[3] = new Track(400);
        obstacles[4] = new Track(1000);

        obstacles[5] = new Wall(1.5f);
        obstacles[6] = new Wall(2.5f);
        obstacles[7] = new Wall(2.25f);
        obstacles[8] = new Wall(3.25f);
        obstacles[9] = new Wall(3.35f);

        Functionalable[] participants = new Functionalable[9];

        participants[0] = new Human("Петя", 5000, 4f);
        participants[1] = new Human("Вася", 4500, 3f);
        participants[2] = new Human("Коля", 6000, 3.35f);

        participants[3] = new Cat("Кузя", 3700, 4.6f);
        participants[4] = new Cat("Барсик", 3400, 4f);
        participants[5] = new Cat("Черныш", 4000, 4.4f);

        participants[6] = new Robot("C-3PO", 3.4f);
        participants[7] = new Robot("R2-D2", 3.35f, 200);
        participants[8] = new Robot("AliExpress", 1f, 35);

        for (Actionable obstacle : obstacles) {
            for (Functionalable participant : participants) {
                obstacle.doAction(participant);
            }
        }

        System.out.println();
        System.out.println("Результаты соревнований:\n");
        for (Functionalable participant : participants) {
            System.out.printf("%s", ((Informable) participant).isCanContinue() ?
                    String.format("%s прошёл всю дистанцию!\n", ((Informable) participant).getName()) :
                    String.format("%s не справился с дистанцией!\n", ((Informable) participant).getName()));
        }
    }
}
