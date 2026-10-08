package contactbook;

public class PersonalContact extends Contact {

    private String relationship;

    public PersonalContact(int id, String name, String phone,
                           String email, String relationship) {
        super(id, name, phone, email);
        this.relationship = relationship;
    }

    public String getRelationship() {
        return relationship;
    }

    @Override
    public String getGroup() {
        return "Personal";
    }

    @Override
    public void showDetails() {
        System.out.println(
                getId() + " | " +
                        getGroup() + " | " +
                        getName() + " | " +
                        getPhone() + " | " +
                        getEmail() + " | Relationship: " +
                        relationship
        );
    }
}