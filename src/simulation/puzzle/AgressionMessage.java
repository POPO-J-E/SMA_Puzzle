package simulation.puzzle;

import madkit.kernel.Message;
import simulation.ex06.SituatedAgent;

import java.awt.*;

/**
 * Created by kifkif on 10/10/2017.
 */
public class AgressionMessage extends Message {
    protected Dimension location;
    protected SituatedAgent agent;

    public AgressionMessage(Dimension location, SituatedAgent agent) {
        this.location = location;
        this.agent = agent;
    }

    public Dimension getLocation() {
        return location;
    }

    public void setLocation(Dimension location) {
        this.location = location;
    }

    public SituatedAgent getAgent() {
        return agent;
    }

    public void setAgent(SituatedAgent agent) {
        this.agent = agent;
    }

    @Override
    public String toString() {
        return "AgressionMessage{" +
                "location=" + location +
                ", agent=" + agent +
                '}';
    }
}
