package app.domain.services;

import app.domain.model.Person;

public class PersonService {

    public boolean validateUniqueId(String idNumber, Iterable<Person> people) {
        for (Person p : people) {
            if (p.getId().equals(idNumber)) {
                return false;
            }
        }
        return true;
    }
}