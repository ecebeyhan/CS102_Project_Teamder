package classes;

public class Rate {

    private double rating;

    //-----------------------------------------------------------------
    //  Getter methods
    //-----------------------------------------------------------------
    public double getRating() {
        return rating;
    }

    //-----------------------------------------------------------------
    //  Setter methods
    //-----------------------------------------------------------------
    public void setRate(double rating) {
        this.rating = rating;
    }

    /**
     * This method is used to calculate the average rating.
     */
    public static double update(double rating, int noOfRaters) {
        double rate = rating / noOfRaters;
        return Math.round(rate * 10.0) / 10.0;
    }
}
