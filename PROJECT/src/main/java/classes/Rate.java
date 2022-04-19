package classes;

public class Rate {

    private int count;
    private double rating;

    //-----------------------------------------------------------------
    //  Getter methods
    //-----------------------------------------------------------------
    public double getRating() {
        return rating;
    }

    public int getCount() {
        return count;
    }
    //-----------------------------------------------------------------
    //  Setter methods
    //-----------------------------------------------------------------
    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setCount(int count) {
        this.count = count;
    }

    /**
     * This method is used to calculate the average rating.
     * @param rate The rating to be added to the current average.
     */
    public void update(int rate) {
        this.rating = (rating * count + rate) / (this.count + 1);
        setCount(count++);
    }
}
