package gb.l3hw;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        String[] words = {"black", "REd", "blue", "red", "Green", "WHIte", "red", "black", "green", "white", "green", "black", "White", "reD",
                "Green"
        };

        HashMap<String, Integer> wordsUnique = new HashMap<>();
        for (String word : words) {
            Integer cnt = wordsUnique.getOrDefault(word.toLowerCase(), 0);
            wordsUnique.put(word.toLowerCase(), cnt + 1);
        }

        System.out.println(wordsUnique);

        PhoneBook phoneBook = new PhoneBook();

        Person person = new Person("Petrov", "+79991112233");
        phoneBook.add(person);
        person = new Person("Sidorov", "+79991112233");
        phoneBook.add(person);
        person = new Person("Petrov", "+71112233444");
        phoneBook.add(person);
        person = new Person("petrov", "+4563456234234");
        phoneBook.add(person);
        person = new Person("sidorov", "+234234324");
        phoneBook.add(person);
        person = new Person("ivanov", "+7878678568");
        phoneBook.add(person);
        person = new Person("Ivanov", "+234234234234");
        phoneBook.add(person);
        person = new Person("Pushkin", "+568568678567");
        phoneBook.add(person);
        person = new Person("Lermontov", "+23423423423");
        phoneBook.add(person);
        person = new Person("Turgenev", "+8797897896789");
        phoneBook.add(person);
        person = new Person("Tolstoj", "+12412341241234");
        phoneBook.add(person);
        person = new Person("Bulgakov", "+4575674567");
        phoneBook.add(person);
        person = new Person("Pushkin", "+6896789768978");
        phoneBook.add(person);
        person = new Person("petroV", "+45674574567456");
        phoneBook.add(person);
        person = new Person("Dostoevskij", "+3452345234545");
        phoneBook.add(person);

        System.out.println(phoneBook.get("pushkin"));
        System.out.println(phoneBook.get("petrov"));
        System.out.println(phoneBook.get("DOSTOEVSKIJ"));
        System.out.println(phoneBook.get("bulgakov"));
        System.out.println(phoneBook.get("tolstoj"));

        person = new Person("TOLStoj", "+12345");
        phoneBook.add(person);

        System.out.println(phoneBook.get("tolstoj"));
    }
}
