package com.mycompany.teaser.phonebook;

import com.mycompany.teaser.util.DatabaseUtil;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PhoneBookImplTest {
    PhoneBookImpl phoneBook;

    @Test
    public void shouldAddFindPerson() {
        DatabaseUtil.initDB();
        phoneBook = new PhoneBookImpl();
        Person p1 = new Person("Zorba TheGreek", "123456789", "123, Crete");
        phoneBook.addPerson(p1);
        Person p2 = phoneBook.findPerson("Zorba", "TheGREEK");
        assertEquals(p1, p2);
    }

    @After
    public void tearDown() {

    }
}
