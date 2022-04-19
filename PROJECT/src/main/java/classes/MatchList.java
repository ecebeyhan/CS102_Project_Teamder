package classes;

import java.util.ArrayList;

public class MatchList {

    ArrayList<Match> matchList;

    public MatchList() {
        this.matchList = new ArrayList<Match>();
    }

    public MatchList(ArrayList<Match> matchList) {
        this.matchList = matchList;
    }

    /**
     * Add a new match to the match list
     * @param match the match to be added
     */
    public void addMatch(Match match) {
        this.matchList.add(match);
    }

    /**
     * Order the match list by activation status
     * @param active true if the match is active, false if the match is inactive
     * @return the match list ordered by activation status
     */
    public MatchList filter(boolean active) {
        MatchList newMatches = new MatchList(); // create new MatchList obj
        for (Match obj: this.matchList) {       // this MatchList'in içinde gez
            if (obj.isActive() == active) {     // maçın aktiflik durumu istenen aktiflik durumuna eşitse
                newMatches.addMatch(obj);       // newMatches MatchList'inin ArrayList'ine ekle
            }
        }
        return newMatches;
    }


//    /**
//     * Filter the match list by the given parameters
//     * @param date
//     * @param place
//     * @param sports
//     * @param name
//     * @return
//     */
//    public MatchList filter(Date date, String place, String[] sports, String name) {
//
//    }


}
