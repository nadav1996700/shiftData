package src.Classes;

public class Worker {

    private String first_name;
    private String last_name;
    private String username;
    private String password;
    private String id;
    private String phone;
    private String company;
    private int age;
    private int photo;

    public Worker(String first_name, String last_name, String username, String password, String id,
                  String phone, String company, int age, int photo) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.password = password;
        this.id = id;
        this.phone = phone;
        this.age = age;
        this.company = company;
        this.photo = photo;
    }

    public Worker() {

    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCompany() {
        return company;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    /*
    @Override
    public String toString() {
        return first_name + " " + last_name;
    }

     */

    @Override
    public String toString() {
        return "Worker{" +
                "first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", id='" + id + '\'' +
                ", phone='" + phone + '\'' +
                ", company='" + company + '\'' +
                ", age=" + age +
                ", photo=" + photo +
                '}';
    }
}
