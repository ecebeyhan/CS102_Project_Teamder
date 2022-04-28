package classes;

import java.util.Date;
import java.time.*;

import java.io.IOException;
import java.sql.SQLException;

public class Match {

    private Sport sport;
    private String place;
    private Date date;
    private LocalDateTime matchDateTime;
    private boolean isActive; //
    private String name;

    /**
     * Create a new match with the given name, sport, place, date, time and automatically active
     * @param name the name of the match
     * @param sport the sport of the match
     * @param place the city the match will take place
     * @param bio the day, month and the year of the match
     * @param startTime the hour the match will start
     * @param endTime the hour the match will end
     */
    public Match(String name, Sport sport, String place, LocalDate date, LocalTime startTime, LocalTime endTime) throws IOException, SQLException{
        setActive(true);
        setPlace(place);
        setSport(sport);
        setName(name);
        setDateTime(date, endTime);
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
    
    //compares endTime with the local time to upregade the boolean active 
    public void compareTme(){
        LocalDateTime local = LocalDateTime.now();
        if(local.isAfter(matchDateTime)){//time of the match has passed
            setActive(false);
        }
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
