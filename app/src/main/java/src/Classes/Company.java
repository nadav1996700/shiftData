package src.Classes;

import androidx.annotation.NonNull;

public class Company {

    private String business_name;
    private String username;
    private String password;

    public Company(String business_name, String username, String password) {
        this.business_name = business_name;
        this.username = username;
        this.password = password;
    }

    public Company() {}

    public String getName() {
        return business_name;
    }

    @NonNull
    @Override
    public String toString() {
        return business_name;
    }
}
