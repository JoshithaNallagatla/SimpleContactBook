# SimpleContactBook

## Project Overview
SimpleContactBook is a Java console-based application for managing personal and work contacts. Users can add, view, search, and update contact information.

## Features
- Add personal contacts with relationship details.
- Add work contacts with company details.
- View all saved contacts.
- Search contacts by name.
- Update existing contacts.
- Validate 10-digit phone numbers.
- Prevent duplicate phone numbers.
- Generate contact summary reports.
- Save contacts and reports in text files.

## Technologies Used
- Java 21
- IntelliJ IDEA
- Object-Oriented Programming
- ArrayList and HashSet
- Java File Handling

## OOP Concepts
- **Encapsulation:** Protects data using private fields and methods.
- **Inheritance:** PersonalContact and WorkContact extend Contact.
- **Abstraction:** Contact defines common contact behavior.
- **Polymorphism:** Different contact types implement their own behavior.

## Project Structure

```text
SimpleContactBook/
├── data/
│   ├── contacts.txt
│   └── report.txt
└── src/
    └── contactbook/
        ├── Contact.java
        ├── PersonalContact.java
        ├── WorkContact.java
        ├── ContactService.java
        ├── FileManager.java
        └── Main.java
```

## Requirements
- JDK 21 or later
- IntelliJ IDEA or another Java IDE

## How to Run
1. Clone or download this repository.
2. Open the project in IntelliJ IDEA.
3. Configure JDK 21 or later.
4. Open `src/contactbook/Main.java`.
5. Click the green Run button.
6. Follow the console menu.

## Data Storage
Contact information is stored in `data/contacts.txt`. Generated reports are stored in `data/report.txt`.

The application uses local text files and does not require an external database or API.

## Author
Joshitha Nallagatla
