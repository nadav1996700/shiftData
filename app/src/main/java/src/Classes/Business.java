package src.Classes;

import java.util.ArrayList;

public class Business {

    private String name;
    private String username;
    private String password;
    private ArrayList<Worker> workers;
    private ArrayList<Shift> current_shifts;
    private ArrayList<Shift> requests;

    public Business(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public ArrayList<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(ArrayList<Worker> workers) {
        this.workers = workers;
    }

    public ArrayList<Shift> getCurrent_shifts() {
        return current_shifts;
    }

    public void setCurrent_shifts(ArrayList<Shift> current_shifts) {
        this.current_shifts = current_shifts;
    }

    public ArrayList<Shift> getRequests() {
        return requests;
    }

    public void setRequests(ArrayList<Shift> requests) {
        this.requests = requests;
    }

    @Override
    public String toString() {
        return name;
    }
}
