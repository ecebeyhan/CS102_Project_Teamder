package classes;

import javafx.scene.control.Hyperlink;

import java.time.*;
import java.sql.Time;

import java.io.IOException;
import java.sql.SQLException;

public class Match {

    private Sport sport;
    private String place;
    private LocalDate date;
    private LocalTime startTime;
    private int duration;
    private LocalDateTime matchDateTime;
    private boolean isActive;
    private String name;
    private Hyperlink matchLink;
    private Hyperlink rateLink;

    /**
     * Create a new match with the given name, sport, place, date, time and automatically active
     * @param name the name of the match
     * @param sport the sport of the match
     * @param place the city the match will take place
     * @param date the day, month and the year of the match
     * @param startTime the hour the match will start
     */
    public Match(String name, Sport sport, String place, LocalDate date, LocalTime startTime, int duration) throws IOException, SQLException{
        setActive(true);
        setPlace(place);
        setSport(sport);
        setName(name);
        this.date = date;
        this.startTime = startTime;
        this.duration = duration;
        setDateTime(date, startTime);
        matchLink = new Hyperlink(name);
        rateLink = new Hyperlink("rate");
    }

    //-----------------------------------------------------------------
    //  Setter methods for all variables
    //-----------------------------------------------------------------
    public void setActive(boolean active) {
        isActive = active;
    }

    public void setDateTime(LocalDate date, LocalTime time) {
        matchDateTime = LocalDateTime.of(date, time);
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
    
    //compares endTime with the local time to upgrade the boolean active
    public void compareTime(){
        LocalDateTime local = LocalDateTime.now();
        if(local.isAfter(matchDateTime)){//time of the match has passed
            setActive(false);
        }
    }

    public LocalTime endTime() {
        return startTime.plusMinutes(this.duration);
    }

    //-----------------------------------------------------------------
    //  Getter methods
    //-----------------------------------------------------------------
    public String getName() {
        return name;
    }

    public Sport getSport() {
        return sport;
    }

    public String getPlace() {
        return place;
    }

    public boolean isActive() {
        return isActive;
    }

    public LocalDateTime getMatchDateTime() {
        return matchDateTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public int getDuration() {
        return duration;
    }

    public Hyperlink getMatchLink(){
        return matchLink;
    }

    public Hyperlink getRateLink(){
        return rateLink;
    }
}
