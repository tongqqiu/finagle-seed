package org.tongqing;

import java.io.FileInputStream;

/**
 * Created by TQui on 11/11/14.
 */
public class ListPerson {
    // Iterates though all people in the AddressBook and prints info about them.
    static void Print(Addressbook.AddressBook addressBook) {
        for (Addressbook.Person person: addressBook.getPersonList()) {
            System.out.println("Person ID: " + person.getId());
            System.out.println("  Name: " + person.getName());
            if (person.hasEmail()) {
                System.out.println("  E-mail address: " + person.getEmail());
            }

            for (Addressbook.Person.PhoneNumber phoneNumber : person.getPhoneList()) {
                switch (phoneNumber.getType()) {
                    case MOBILE:
                        System.out.print("  Mobile phone #: ");
                        break;
                    case HOME:
                        System.out.print("  Home phone #: ");
                        break;
                    case WORK:
                        System.out.print("  Work phone #: ");
                        break;
                }
                System.out.println(phoneNumber.getNumber());
            }
        }
    }

    // Main function:  Reads the entire address book from a file and prints all
    //   the information inside.
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage:  ListPeople ADDRESS_BOOK_FILE");
            System.exit(-1);
        }

        // Read the existing address book.
        Addressbook.AddressBook addressBook =
                Addressbook.AddressBook.parseFrom(new FileInputStream(args[0]));

        Print(addressBook);
    }
}
