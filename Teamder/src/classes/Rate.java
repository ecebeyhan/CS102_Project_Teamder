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

    //-----------------------------------------------------------------
    //  Update method to updating the rate of the user
    //-----------------------------------------------------------------
    public void update(int rate) {
        this.rating = (rating * count + rate) / (this.count + 1);
        setCount(count++);
    }
}
