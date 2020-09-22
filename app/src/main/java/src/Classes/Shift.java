package src.Classes;

import java.util.ArrayList;

public class Shift {
    private String date;
    private ArrayList<Worker> morning_shift;
    private ArrayList<Worker> evening_shift;
    private ArrayList<Worker> night_shift;

    public Shift() {}

    public Shift(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public ArrayList<Worker> getMorning_shift() {
        return morning_shift;
    }

    public ArrayList<Worker> getEvening_shift() {
        return evening_shift;
    }

    public ArrayList<Worker> getNight_shift() {
        return night_shift;
    }
}
