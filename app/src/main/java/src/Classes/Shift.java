package src.Classes;

import java.util.ArrayList;
import java.util.Date;

public class Shift {
    private Date date;
    private ArrayList<Worker> morning_shift;
    private ArrayList<Worker> evening_shift;
    private ArrayList<Worker> night_shift;

    public Shift(Date date) {
        this.date = date;
    }

    public Date getDate() {
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
