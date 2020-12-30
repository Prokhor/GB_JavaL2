package gb.l3hw;

import java.util.ArrayList;
import java.util.List;

public class PhoneBook {
    private List<Person> persons;

    public PhoneBook() {
        persons = new ArrayList<>();
    }

    public void add(Person person){
        persons.add(person);
    }

    public List<Person> get(String surname){
        List<Person> result = new ArrayList<>();
        for (Person person: persons) {
            if (person.getSurname().equals(surname.toLowerCase())) {
                result.add(person);
            }
        }
        return result;
    }
}