package contactbook;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FileManager {

    private final Path dataFolder;
    private final Path contactsFile;
    private final Path reportFile;

    public FileManager() throws IOException {

        dataFolder = Path.of("data");
        contactsFile = dataFolder.resolve("contacts.txt");
        reportFile = dataFolder.resolve("report.txt");

        // Create data folder if it does not exist
        Files.createDirectories(dataFolder);
    }

    // Load contacts from contacts.txt
    public List<Contact> loadContacts() throws IOException {

        List<Contact> contacts = new ArrayList<>();
        HashSet<String> phones = new HashSet<>();
        HashSet<Integer> ids = new HashSet<>();

        // If contacts.txt does not exist, return empty list
        if (!Files.exists(contactsFile)) {
            return contacts;
        }

        try (BufferedReader reader = Files.newBufferedReader(
                contactsFile,
                StandardCharsets.UTF_8)) {

            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {

                lineNumber++;

                // Ignore empty lines
                if (line.isBlank()) {
                    continue;
                }

                // Split into 6 fields
                String[] fields = line.split("\\|", -1);

                if (fields.length != 6) {
                    throw new IOException(
                            "Invalid data in contacts.txt at line "
                                    + lineNumber
                                    + ". Six fields required."
                    );
                }

                int id;

                try {
                    id = Integer.parseInt(fields[0]);
                } catch (NumberFormatException e) {
                    throw new IOException(
                            "Invalid contact ID at line "
                                    + lineNumber
                    );
                }

                String group = fields[1];
                String name = fields[2];
                String phone = fields[3];
                String email = fields[4];
                String detail = fields[5];

                // Validate data
                validateLoadedContact(
                        id,
                        group,
                        name,
                        phone,
                        detail,
                        lineNumber
                );

                // Check duplicate ID
                if (!ids.add(id)) {
                    throw new IOException(
                            "Duplicate contact ID at line "
                                    + lineNumber
                    );
                }

                // Check duplicate phone
                if (!phones.add(phone)) {
                    throw new IOException(
                            "Duplicate phone number at line "
                                    + lineNumber
                    );
                }

                Contact contact;

                if (group.equals("Personal")) {

                    contact = new PersonalContact(
                            id,
                            name,
                            phone,
                            email,
                            detail
                    );

                } else {

                    contact = new WorkContact(
                            id,
                            name,
                            phone,
                            email,
                            detail
                    );
                }

                contacts.add(contact);
            }
        }

        return contacts;
    }

    // Validate contact loaded from file
    private void validateLoadedContact(
            int id,
            String group,
            String name,
            String phone,
            String detail,
            int lineNumber) throws IOException {

        if (id <= 0) {
            throw new IOException(
                    "ID must be positive at line "
                            + lineNumber
            );
        }

        if (!group.equals("Personal")
                && !group.equals("Work")) {

            throw new IOException(
                    "Invalid group at line "
                            + lineNumber
            );
        }

        if (name.isBlank()) {
            throw new IOException(
                    "Name cannot be blank at line "
                            + lineNumber
            );
        }

        if (!phone.matches("\\d{10}")) {
            throw new IOException(
                    "Phone must contain exactly 10 digits at line "
                            + lineNumber
            );
        }

        if (detail.isBlank()) {
            throw new IOException(
                    "Relationship/company cannot be blank at line "
                            + lineNumber
            );
        }

        if (containsInvalidCharacters(name)
                || containsInvalidCharacters(phone)
                || containsInvalidCharacters(detail)) {

            throw new IOException(
                    "Invalid characters at line "
                            + lineNumber
            );
        }
    }

    // Save all contacts into contacts.txt
    public void saveContacts(List<Contact> contacts)
            throws IOException {

        Path tempFile =
                dataFolder.resolve("contacts.tmp");

        try (BufferedWriter writer =
                     Files.newBufferedWriter(
                             tempFile,
                             StandardCharsets.UTF_8)) {

            for (Contact contact : contacts) {

                String detail;

                if (contact instanceof PersonalContact) {

                    PersonalContact personal =
                            (PersonalContact) contact;

                    detail =
                            personal.getRelationship();

                } else if (contact instanceof WorkContact) {

                    WorkContact work =
                            (WorkContact) contact;

                    detail =
                            work.getCompany();

                } else {

                    throw new IOException(
                            "Unknown contact type."
                    );
                }

                validateForSaving(
                        contact.getName(),
                        contact.getPhone(),
                        contact.getEmail(),
                        detail
                );

                writer.write(
                        contact.getId()
                                + "|"
                                + contact.getGroup()
                                + "|"
                                + contact.getName()
                                + "|"
                                + contact.getPhone()
                                + "|"
                                + contact.getEmail()
                                + "|"
                                + detail
                );

                writer.newLine();
            }
        }

        // Replace old file with new file
        Files.move(
                tempFile,
                contactsFile,
                StandardCopyOption.REPLACE_EXISTING
        );
    }

    // Validate data before saving
    private void validateForSaving(
            String name,
            String phone,
            String email,
            String detail) throws IOException {

        if (name == null || name.isBlank()) {
            throw new IOException(
                    "Name cannot be blank."
            );
        }

        if (phone == null
                || !phone.matches("\\d{10}")) {

            throw new IOException(
                    "Phone must contain exactly 10 digits."
            );
        }

        if (email == null) {
            throw new IOException(
                    "Email cannot be null."
            );
        }

        if (detail == null || detail.isBlank()) {
            throw new IOException(
                    "Relationship/company cannot be blank."
            );
        }

        if (containsInvalidCharacters(name)
                || containsInvalidCharacters(phone)
                || containsInvalidCharacters(email)
                || containsInvalidCharacters(detail)) {

            throw new IOException(
                    "Fields cannot contain | or line breaks."
            );
        }
    }

    // Check invalid characters
    private boolean containsInvalidCharacters(
            String value) {

        return value.contains("|")
                || value.contains("\n")
                || value.contains("\r");
    }

    // Save report.txt
    public void saveReport(String report)
            throws IOException {

        try (BufferedWriter writer =
                     Files.newBufferedWriter(
                             reportFile,
                             StandardCharsets.UTF_8)) {

            writer.write(report);
        }
    }
}