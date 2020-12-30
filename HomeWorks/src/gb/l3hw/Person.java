package gb.l3hw;

public class Person {
    private String surname;
    private String phoneNumber;

    public Person(String surname, String phoneNumber) {
        this.surname = surname;
        this.phoneNumber = phoneNumber;
    }

    public String getSurname() {
        return surname.toLowerCase();
    }

    @Override
    public String toString() {
        return String.format("%s: %s", surname, phoneNumber);
    }
}
