package gb.l1ahw;

public class Main {

    private static final int HOURS_WEEK = 40;
    private static final int HOURS_DAY = 8;

    public static void main(String[] args) {
        System.out.println(getWorkingHours(DaysOfWeek.Friday));
    }

    private static String getWorkingHours(DaysOfWeek dayOfWeek) {
        if (dayOfWeek.ordinal() <= 4) {
            return String.format("До конца рабочей недели осталось %d часов!", HOURS_WEEK - dayOfWeek.ordinal() * HOURS_DAY);
        }
        return "Сегодня выходной!";
    }
}
