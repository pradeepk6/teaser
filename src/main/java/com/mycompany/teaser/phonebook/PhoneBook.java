package com.mycompany.teaser.phonebook;

public interface PhoneBook {
    Person findPerson(String firstName, String lastName);

    void addPerson(Person newPerson);
}
