package contactbook;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    // One Scanner for the complete program
    private static final Scanner scanner =
            new Scanner(System.in);

    public static void main(String[] args) {

        try {

            // Create FileManager
            FileManager fileManager =
                    new FileManager();

            // Create ContactService
            ContactService service =
                    new ContactService(fileManager);

            // Start menu
            showMenu(service);

        } catch (IOException e) {

            System.out.println(
                    "File error: " + e.getMessage()
            );
        }

        scanner.close();
    }

    // Main menu
    public static void showMenu(
            ContactService service) {

        while (true) {

            System.out.println();
            System.out.println(
                    "================================"
            );
            System.out.println(
                    "        SIMPLE CONTACT BOOK"
            );
            System.out.println(
                    "================================"
            );

            System.out.println(
                    "1. Add personal contact"
            );

            System.out.println(
                    "2. Add work contact"
            );

            System.out.println(
                    "3. View all contacts"
            );

            System.out.println(
                    "4. Search by name"
            );

            System.out.println(
                    "5. Update contact"
            );

            System.out.println(
                    "6. View reports"
            );

            System.out.println(
                    "0. Exit"
            );

            System.out.print(
                    "Enter your choice: "
            );

            String choice =
                    scanner.nextLine().trim();

            try {

                switch (choice) {

                    case "1":
                        addPersonal(service);
                        break;

                    case "2":
                        addWork(service);
                        break;

                    case "3":
                        service.viewAllContacts();
                        break;

                    case "4":
                        searchContact(service);
                        break;

                    case "5":
                        updateContact(service);
                        break;

                    case "6":
                        showReports(service);
                        break;

                    case "0":

                        System.out.println(
                                "Thank you for using Contact Book."
                        );

                        return;

                    default:

                        System.out.println(
                                "Invalid choice. Please try again."
                        );
                }

            } catch (IOException |
                     IllegalArgumentException e) {

                System.out.println(
                        "Error: " + e.getMessage()
                );
            }
        }
    }

    // Add Personal Contact
    private static void addPersonal(
            ContactService service)
            throws IOException {

        System.out.println();
        System.out.println(
                "--- ADD PERSONAL CONTACT ---"
        );

        String name =
                readRequired("Name: ");

        String phone =
                readPhone();

        String email =
                readEmail();

        String relationship =
                readRequired(
                        "Relationship: "
                );

        service.addPersonalContact(
                name,
                phone,
                email,
                relationship
        );
    }

    // Add Work Contact
    private static void addWork(
            ContactService service)
            throws IOException {

        System.out.println();
        System.out.println(
                "--- ADD WORK CONTACT ---"
        );

        String name =
                readRequired("Name: ");

        String phone =
                readPhone();

        String email =
                readEmail();

        String company =
                readRequired(
                        "Company: "
                );

        service.addWorkContact(
                name,
                phone,
                email,
                company
        );
    }

    // Search Contact
    private static void searchContact(
            ContactService service) {

        String keyword =
                readRequired(
                        "Search name: "
                );

        service.searchByName(keyword);
    }

    // Update Contact
    private static void updateContact(
            ContactService service)
            throws IOException {

        System.out.println();
        System.out.println(
                "--- UPDATE CONTACT ---"
        );

        int id =
                readPositiveInt(
                        "Contact ID: "
                );

        // Find contact group
        String group =
                service.getContactGroup(id);

        if (group == null) {

            System.out.println(
                    "Contact ID not found."
            );

            return;
        }

        String name =
                readRequired(
                        "New name: "
                );

        String phone =
                readPhone();

        String email =
                readEmail();

        String detail;

        if (group.equals("Personal")) {

            detail =
                    readRequired(
                            "New relationship: "
                    );

        } else {

            detail =
                    readRequired(
                            "New company: "
                    );
        }

        service.updateContact(
                id,
                name,
                phone,
                email,
                detail
        );
    }

    // Reports
    private static void showReports(
            ContactService service)
            throws IOException {

        service.showReport();

        System.out.print(
                "Save report to report.txt? (Y/N): "
        );

        String answer =
                scanner.nextLine().trim();

        if (answer.equalsIgnoreCase("Y")) {

            service.saveReport();
        }
    }

    // Read required text
    private static String readRequired(
            String message) {

        while (true) {

            System.out.print(message);

            String value =
                    scanner.nextLine().trim();

            if (!value.isEmpty()) {

                return value;
            }

            System.out.println(
                    "This field cannot be blank."
            );
        }
    }

    // Read phone number
    private static String readPhone() {

        while (true) {

            System.out.print(
                    "Phone with exactly 10 digits: "
            );

            String phone =
                    scanner.nextLine().trim();

            if (phone.matches("\\d{10}")) {

                return phone;
            }

            System.out.println(
                    "Invalid phone. Enter exactly 10 digits."
            );
        }
    }

    // Read email
    private static String readEmail() {

        System.out.print(
                "Email or blank: "
        );

        return scanner.nextLine().trim();
    }

    // Read positive integer
    private static int readPositiveInt(
            String message) {

        while (true) {

            System.out.print(message);

            String value =
                    scanner.nextLine().trim();

            try {

                int number =
                        Integer.parseInt(value);

                if (number > 0) {

                    return number;
                }

            } catch (NumberFormatException e) {

                // Invalid number
            }

            System.out.println(
                    "Please enter a positive number."
            );
        }
    }
}