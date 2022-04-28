package classes;

public abstract class Sport {

    final int TEAM_A = 0;
    final int TEAM_B = 1;

    protected User[] teamA; // User arrays for both teams
    protected User[] teamB;
    protected String image; // uygun classla değişilecek

    public void addPlayer(int team, int position) {
        if (team == TEAM_A) {

        }
    }

    public void removePlayer(int team, int position) {

    }

    public void setTeams(int playerCount) {

    }

    public abstract String getName();
}
