package com.mycompany.teaser.phonebook;

import com.mycompany.teaser.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PhoneBookImpl implements PhoneBook {
    public List<Person> people;

    public PhoneBookImpl() {
        people = findAll();
    }

    public static void main(String[] args) {
        DatabaseUtil.initDB();  //You should not remove this line, it creates the in-memory database
        PhoneBookImpl phoneBook = new PhoneBookImpl();

        /*  create person objects and put them in the PhoneBook and database
         * John Smith, (248) 123-4567, 1234 Sand Hill Dr, Royal Oak, MI
         * Cynthia Smith, (824) 128-8758, 875 Main St, Ann Arbor, MI
         */
        Person p1 = new Person("John Smith", "(248) 123-4567", "1234 Sand Hill Dr, Royal Oak, MI");
        Person p2 = new Person("Cynthia Smith", "(824) 128-8758", "875 Main St, Ann Arbor, MI");
        phoneBook.addPerson(p1);
        phoneBook.addPerson(p2);

        //  print the phone book out to System.out
        System.out.println("*** Begin of Phone Book print ***");
        for (Person p : phoneBook.people) {
            System.out.println(p.toString());
        }
        System.out.println("*** End of Phone Book print ***");

        //  find Cynthia Smith and print out just her entry
        Person p = phoneBook.findPerson("Cynthia", "Smith");
        System.out.println(p.toString());

        //  insert the new person objects into the database
    }

    @Override
    public void addPerson(Person newPerson) {
        try {
            Connection con = DatabaseUtil.getConnection();
            PreparedStatement ps = con.prepareStatement("INSERT INTO PHONEBOOK (NAME, PHONENUMBER, ADDRESS) VALUES (?,?,?)");
            ps.setString(1, newPerson.getName());
            ps.setString(2, newPerson.getPhoneNumber());
            ps.setString(3, newPerson.getAddress());
            int numRowsAffected = ps.executeUpdate();
            con.commit();
            if (numRowsAffected == 1) people.add(newPerson);
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Person findPerson(String firstName, String lastName) {
        Person person = null;
        try {
            Connection con = DatabaseUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM PHONEBOOK pb where UPPER(pb.name) LIKE ? AND UPPER(pb.name) LIKE ? ");
            ps.setString(1, "%" + firstName.toUpperCase() + "%");
            ps.setString(2, "%" + lastName.toUpperCase() + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                person = new Person(rs.getString(1), rs.getString(2), rs.getString(3));
            }
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return person;
    }

    public List<Person> findAll() {
        List<Person> personList = new ArrayList<>();
        try {
            Connection con = DatabaseUtil.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM PHONEBOOK");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Person p = new Person(rs.getString(1), rs.getString(2), rs.getString(3));
                personList.add(p);
            }
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return personList;
    }
}
