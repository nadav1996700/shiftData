package src.Classes;

import androidx.annotation.NonNull;

public class Worker {

    private String first_name;
    private String last_name;
    private String username;
    private String password;
    private String id;
    private String phone;
    private int age;

    public Worker(String first_name, String last_name, String username, String password, String id,
                  String phone, int age) {
        setFirst_name(first_name);
        setLast_name(last_name);
        setUsername(username);
        setPassword(password);
        setId(id);
        setPhone(phone);
        setAge(age);
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

    @NonNull
    @Override
    public String toString() {
        return first_name + " " + last_name;
    }
}
