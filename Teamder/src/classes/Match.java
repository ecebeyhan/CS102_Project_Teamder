package classes;

import java.util.Date;

public class Match {

    private Sport sport;
    private String place;
    private Date date; // uygun bir class'la değişecez
    private boolean isActive; //
    private String name;
    private int ID;

    public Match(Sport sport, String place, Date date, boolean isActive, String name, int ID) {
        this.sport = sport;
        this.place = place;
        this.date = date;
        this.isActive = isActive;
        this.name = name;
        this.ID = ID;
    }

    //-----------------------------------------------------------------
    //  Setter methods for all variables
    //-----------------------------------------------------------------
    public void setActive(boolean active) {
        isActive = active;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    //-----------------------------------------------------------------
    //  Getter methods
    //-----------------------------------------------------------------
    public String getName() {
        return name;
    }

    public int getID() {
        return ID;
    }

    public Sport getSport() {
        return sport;
    }

    public Date getDate() {
        return date;
    }

    public String getPlace() {
        return place;
    }

    public boolean isActive() {
        return isActive;
    }
}
