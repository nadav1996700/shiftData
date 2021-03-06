package src.Classes;

import androidx.annotation.NonNull;

public class DataItem {
    private String id;
    private String first_name;
    private String last_name;

    public DataItem(String id, String first_name, String last_name) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
    }

    public String getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    @NonNull
    @Override
    public String toString() {
        return first_name + " " + last_name;
    }
}
