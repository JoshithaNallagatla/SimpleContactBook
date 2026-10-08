package contactbook;

public class WorkContact extends Contact {

    private String company;

    public WorkContact(int id, String name, String phone,
                       String email, String company) {
        super(id, name, phone, email);
        this.company = company;
    }

    public String getCompany() {
        return company;
    }

    @Override
    public String getGroup() {
        return "Work";
    }

    @Override
    public void showDetails() {
        System.out.println(
                getId() + " | " +
                        getGroup() + " | " +
                        getName() + " | " +
                        getPhone() + " | " +
                        getEmail() + " | Company: " +
                        company
        );
    }
}