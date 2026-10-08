package contactbook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class ContactService {

    private final ArrayList<Contact> contacts;
    private final HashSet<String> phones;

    private final FileManager fileManager;

    private int nextId;

    public ContactService(FileManager fileManager)
            throws IOException {

        this.fileManager = fileManager;

        this.contacts = new ArrayList<>();
        this.phones = new HashSet<>();

        loadData();
    }

    private void loadData() throws IOException {

        List<Contact> loadedContacts =
                fileManager.loadContacts();

        contacts.clear();
        phones.clear();

        int maximumId = 0;

        for (Contact contact : loadedContacts) {

            contacts.add(contact);
            phones.add(contact.getPhone());

            if (contact.getId() > maximumId) {
                maximumId = contact.getId();
            }
        }

        nextId = maximumId + 1;
    }

    public void addPersonalContact(
            String name,
            String phone,
            String email,
            String relationship)
            throws IOException {

        validateContactDetails(
                name,
                phone,
                email,
                relationship
        );

        if (phones.contains(phone)) {
            throw new IllegalArgumentException(
                    "Phone number already exists."
            );
        }

        Contact contact = new PersonalContact(
                nextId,
                name,
                phone,
                email,
                relationship
        );

        ArrayList<Contact> newList =
                new ArrayList<>(contacts);

        newList.add(contact);

        fileManager.saveContacts(newList);

        contacts.add(contact);
        phones.add(phone);
        nextId++;

        System.out.println(
                "Contact " + contact.getId() + " saved."
        );
    }

    public void addWorkContact(
            String name,
            String phone,
            String email,
            String company)
            throws IOException {

        validateContactDetails(
                name,
                phone,
                email,
                company
        );

        if (phones.contains(phone)) {
            throw new IllegalArgumentException(
                    "Phone number already exists."
            );
        }

        Contact contact = new WorkContact(
                nextId,
                name,
                phone,
                email,
                company
        );

        ArrayList<Contact> newList =
                new ArrayList<>(contacts);

        newList.add(contact);

        fileManager.saveContacts(newList);

        contacts.add(contact);
        phones.add(phone);
        nextId++;

        System.out.println(
                "Contact " + contact.getId() + " saved."
        );
    }

    public void viewAllContacts() {

        if (contacts.isEmpty()) {
            System.out.println("No records found.");
            return;
        }

        System.out.println("\n--- ALL CONTACTS ---");

        for (Contact contact : contacts) {
            contact.showDetails();
        }
    }

    public void searchByName(String keyword) {

        if (contacts.isEmpty()) {
            System.out.println("No records found.");
            return;
        }

        String search =
                keyword.toLowerCase(Locale.ROOT);

        boolean found = false;

        System.out.println("\n--- SEARCH RESULTS ---");

        for (Contact contact : contacts) {

            String name =
                    contact.getName()
                            .toLowerCase(Locale.ROOT);

            if (name.contains(search)) {

                contact.showDetails();
                found = true;
            }
        }

        if (!found) {
            System.out.println(
                    "No matching contacts found."
            );
        }
    }

    public void updateContact(
            int id,
            String name,
            String phone,
            String email,
            String detail)
            throws IOException {

        int index = findContactIndex(id);

        if (index == -1) {
            throw new IllegalArgumentException(
                    "Contact ID not found."
            );
        }

        Contact oldContact = contacts.get(index);

        validateContactDetails(
                name,
                phone,
                email,
                detail
        );

        if (!phone.equals(oldContact.getPhone())
                && phones.contains(phone)) {

            throw new IllegalArgumentException(
                    "Phone number already belongs to another contact."
            );
        }

        Contact replacement;

        if (oldContact instanceof PersonalContact) {

            replacement = new PersonalContact(
                    oldContact.getId(),
                    name,
                    phone,
                    email,
                    detail
            );

        } else {

            replacement = new WorkContact(
                    oldContact.getId(),
                    name,
                    phone,
                    email,
                    detail
            );
        }

        ArrayList<Contact> newList =
                new ArrayList<>(contacts);

        newList.set(index, replacement);

        fileManager.saveContacts(newList);

        contacts.set(index, replacement);

        rebuildPhoneSet();

        System.out.println(
                "Contact saved with ID " + id + "."
        );
    }

    private int findContactIndex(int id) {

        for (int i = 0; i < contacts.size(); i++) {

            if (contacts.get(i).getId() == id) {
                return i;
            }
        }

        return -1;
    }

    private void rebuildPhoneSet() {

        phones.clear();

        for (Contact contact : contacts) {
            phones.add(contact.getPhone());
        }
    }

    private void validateContactDetails(
            String name,
            String phone,
            String email,
            String detail) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Name cannot be blank."
            );
        }

        if (phone == null ||
                !phone.matches("\\d{10}")) {

            throw new IllegalArgumentException(
                    "Phone must contain exactly 10 digits."
            );
        }

        if (email == null) {
            throw new IllegalArgumentException(
                    "Email cannot be null."
            );
        }

        if (detail == null || detail.isBlank()) {
            throw new IllegalArgumentException(
                    "Relationship/company cannot be blank."
            );
        }

        if (containsInvalidCharacters(name)
                || containsInvalidCharacters(phone)
                || containsInvalidCharacters(email)
                || containsInvalidCharacters(detail)) {

            throw new IllegalArgumentException(
                    "Fields cannot contain | or line breaks."
            );
        }
    }

    private boolean containsInvalidCharacters(
            String value) {

        return value.contains("|")
                || value.contains("\n")
                || value.contains("\r");
    }

    public String showReport() {

        String[] groups = {
                "Personal",
                "Work"
        };

        int[] counts =
                new int[groups.length];

        for (Contact contact : contacts) {

            int index =
                    contact.getGroup()
                            .equals("Personal")
                            ? 0 : 1;

            counts[index]++;
        }

        int totalContacts = contacts.size();

        int uniquePhones = phones.size();

        StringBuilder report =
                new StringBuilder();

        report.append("CONTACT BOOK REPORT\n");
        report.append("===================\n");
        report.append("Total contacts: ")
                .append(totalContacts)
                .append("\n");

        for (int i = 0; i < groups.length; i++) {

            report.append(groups[i])
                    .append(": ")
                    .append(counts[i])
                    .append("\n");
        }

        report.append("Unique phones: ")
                .append(uniquePhones)
                .append("\n");

        System.out.println("\n" + report);

        return report.toString();
    }

    public void saveReport()
            throws IOException {

        String report = showReport();

        fileManager.saveReport(report);

        System.out.println(
                "Report saved successfully."
        );
    }
    public String getContactGroup(int id) {

        for (Contact contact : contacts) {

            if (contact.getId() == id) {
                return contact.getGroup();
            }
        }

        return null;
    }
}