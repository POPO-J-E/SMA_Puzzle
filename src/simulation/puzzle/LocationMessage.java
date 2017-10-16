package simulation.puzzle;

import madkit.kernel.Message;

import java.awt.*;

/**
 * Created by kifkif on 10/10/2017.
 */
public class LocationMessage extends Message {
    protected Dimension location;

    public LocationMessage(Dimension location) {
        this.location = location;
    }

    public Dimension getLocation() {
        return location;
    }

    public void setLocation(Dimension location) {
        this.location = location;
    }

    public String toString() {
        String s = super.toString();
        return s + " | " + this.getLocation();
    }
}
