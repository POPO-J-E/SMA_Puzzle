package simulation.puzzle;

import madkit.kernel.Message;

import java.awt.*;

/**
 * Created by kifkif on 10/10/2017.
 */
public class AgressionMessage extends Message {
    protected Dimension constraint;
    protected Dimension from;

    public AgressionMessage(Dimension from, Dimension constraint) {
        this.from = from;
        this.constraint = constraint;
    }

    public Dimension getConstraint() {
        return constraint;
    }

    public Dimension getFrom() {
        return from;
    }

    @Override
    public String toString() {
        return "AgressionMessage{" +
                "constraint=" + constraint +
                "from=" + from +
                '}';
    }
}
