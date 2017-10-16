package simulation.ex06;

import madkit.kernel.AbstractAgent;
import madkit.kernel.Message;
import simulation.puzzle.LocationMessage;

import java.awt.*;
import java.util.Random;

public class SituatedAgent extends AbstractAgent {

    /**
     * The agent's environment. Here it is just used to know its boundaries. It will be automatically set by the environment
     * agent itself: No need to instantiate anything here.
     */
    protected EnvironmentAgent environment;

    /**
     * agent's position
     */
    protected Dimension location = new Dimension();
    protected Dimension target = new Dimension();



    protected Color color;

    /**
     * initialize my role and fields
     */
    @Override
    protected void activate() {
        requestRole(MySimulationModel.MY_COMMUNITY, MySimulationModel.SIMU_GROUP, MySimulationModel.AGENT_ROLE);
        Dimension envDim = environment.getDimension();

        location.width = (int) (Math.random() * envDim.width);
        location.height = (int) (Math.random() * envDim.height);

        target.width = (int) (Math.random() * envDim.width);
        target.height = (int) (Math.random() * envDim.height);

        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        color = new Color(r, g, b);
    }

    /**
     * A non sense behavior, just moving around.
     */
    @SuppressWarnings("unused")
    private void doIt() {
        while (!isMessageBoxEmpty())
        {
            Message message = nextMessage();
            System.out.println(message);
        }

        Dimension envDim = environment.getDimension();
        Dimension newLoc = new Dimension(location);

        if(newLoc.width > target.width)
        {
            newLoc.width--;
        }
        else if(newLoc.width < target.width)
        {
            newLoc.width++;
        }
        if(newLoc.height > target.height)
        {
            newLoc.height--;
        }
        else if(newLoc.height < target.height)
        {
            newLoc.height++;
        }
        newLoc.width %= envDim.width;
        newLoc.height %= envDim.height;

        AbstractAgent agent = environment.getAgentAt(newLoc, this);
        if(agent != null)
        {
            System.out.println("cant go at "+newLoc);
            sendMessage(agent.getAgentAddressIn(MySimulationModel.MY_COMMUNITY, MySimulationModel.SIMU_GROUP, MySimulationModel.AGENT_ROLE), new LocationMessage(newLoc));
        }
        else
        {
            location.width = newLoc.width;
            location.height = newLoc.height;
        }
    }

    public Dimension getLocation() {
        return location;
    }

    public Dimension getTarget() {
        return target;
    }

    public Color getColor() {
        return color;
    }
}
