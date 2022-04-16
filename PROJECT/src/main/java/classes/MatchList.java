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

    //-----------------------------------------------------------------
    //  Add new "Match" to "matchList" arraylist
    //-----------------------------------------------------------------
    public void addMatch(Match match) {
        this.matchList.add(match);
    }

    //-----------------------------------------------------------------
    //  Filter the matches in "this"
    //  Maçları aktiflik durumuna göre return
    //-----------------------------------------------------------------
    public MatchList filter(boolean active) {
        MatchList newMatches = new MatchList(); // create new MatchList obj
        for (Match obj: this.matchList) {       // this MatchList'in içinde gez
            if (obj.isActive() == active) {     // maçın aktiflik durumu istenen aktiflik durumuna eşitse
                newMatches.addMatch(obj);       // newMatches MatchList'inin ArrayList'ine ekle
            }
        }
        return newMatches;
    }

    //-----------------------------------------------------------------
    //  Filter the matches in "this"
    //  Maçları verilen filtreye göre return
    //-----------------------------------------------------------------

    //public MatchList filter(Date date, String place, String[] sports, String name) {

    //}


}
