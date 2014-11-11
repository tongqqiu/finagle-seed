package org.tongqing;


import java.io.*;

/**
 * Created by TQui on 11/11/14.
 */
public class AddPerson {

    static Addressbook.Person PromptForAddress(BufferedReader stdin,
                                   PrintStream stdout) throws IOException {

        Addressbook.Person.Builder person = Addressbook.Person.newBuilder();

        stdout.print("Enter person id:");
        person.setId(Integer.valueOf(stdin.readLine()));

        stdout.print("Enter name: ");
        person.setName(stdin.readLine());

        stdout.print("Enter email address (blank for none): ");
        String email = stdin.readLine();
        if (email.length() > 0) {
            person.setEmail(email);
        }

        while (true) {
            stdout.print("Enter a phone number (or leave blank to finish): ");
            String number = stdin.readLine();
            if (number.length() == 0) {
                break;
            }

            Addressbook.Person.PhoneNumber.Builder phoneNumber =
                    Addressbook.Person.PhoneNumber.newBuilder().setNumber(number);

            stdout.print("Is this a mobile, home, or work phone? ");
            String type = stdin.readLine();
            if (type.equals("mobile")) {
                phoneNumber.setType(Addressbook.Person.PhoneType.MOBILE);
            } else if (type.equals("home")) {
                phoneNumber.setType(Addressbook.Person.PhoneType.HOME);
            } else if (type.equals("work")) {
                phoneNumber.setType(Addressbook.Person.PhoneType.WORK);
            } else {
                stdout.println("Unknown phone type.  Using default.");
            }

            person.addPhone(phoneNumber);
        }

        return person.build();
    }

    // Main function:  Reads the entire address book from a file,
    //   adds one person based on user input, then writes it back out to the same
    //   file.
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage:  AddPerson ADDRESS_BOOK_FILE");
            System.exit(-1);
        }

        Addressbook.AddressBook.Builder addressBook = Addressbook.AddressBook.newBuilder();

        // Read the existing address book.
        try {
            addressBook.mergeFrom(new FileInputStream(args[0]));
        } catch (FileNotFoundException e) {
            System.out.println(args[0] + ": File not found.  Creating a new file.");
        }

        // Add an address.
        addressBook.addPerson(
                PromptForAddress(new BufferedReader(new InputStreamReader(System.in)),
                        System.out));

        // Write the new address book back to disk.
        FileOutputStream output = new FileOutputStream(args[0]);
        addressBook.build().writeTo(output);
        output.close();
    }
}
