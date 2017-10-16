package simulation.ex06;

import madkit.kernel.AbstractAgent;
import madkit.kernel.Message;
import simulation.puzzle.DistanceDimension;

import java.awt.*;
import java.util.ArrayList;
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
    protected Dimension previousLocation = new Dimension(-1,-1);
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

        if (calcDistance(location)==0)
            return;

        Dimension envDim = environment.getDimension();

        Dimension d1 = new Dimension(location.width,location.height+1);
        Dimension d2 = new Dimension(location.width+1,location.height);
        Dimension d3 = new Dimension(location.width,location.height-1);
        Dimension d4 = new Dimension(location.width-1,location.height);

        Dimension[] dimensions = {// top, right, bottom, left
                d1,
                d2,
                d3,
                d4,
        };

        ArrayList<DistanceDimension> sortedDistanceDimensions = new ArrayList<>();

        for (Dimension dim : dimensions)
        {
            int i = 0;
            boolean done = false;
            DistanceDimension newDistanceDimension = new DistanceDimension(dim,calcDistance(dim));
            for (DistanceDimension distanceDimension : sortedDistanceDimensions)
            {
                if (isInMap(dim) && distanceDimension.getDistance() > newDistanceDimension.getDistance())
                {
                    sortedDistanceDimensions.add(i,newDistanceDimension);
                    done = true;
                    break;
                }
                i++;

            }
            if (!done && isInMap(dim))
                sortedDistanceDimensions.add(newDistanceDimension);
        }

        for (DistanceDimension distanceDimension : sortedDistanceDimensions){
            Dimension newLoc = distanceDimension.getDimension();
//            newLoc.width %= envDim.width;
//            newLoc.height %= envDim.height;

            AbstractAgent agent = environment.getAgentAt(newLoc, this);
            if(agent != null || (newLoc.width == previousLocation.width && newLoc.height == previousLocation.height))
            {

                System.out.println("cant go at "+newLoc);
//            sendMessage(agent.getAgentAddressIn(MySimulationModel.MY_COMMUNITY, MySimulationModel.SIMU_GROUP, MySimulationModel.AGENT_ROLE), new LocationMessage(newLoc));
            }
            else
            {
                previousLocation.width = location.width;
                previousLocation.height = location.height;

                location.width = newLoc.width;
                location.height = newLoc.height;
                return;
            }
        }
    }

    private boolean isInMap(Dimension dim) {
        boolean er = dim.width >=0 && dim.height>= 0 && dim.width<environment.getDimension().width && dim.height<environment.getDimension().height;
        return er;
    }


    private int calcDistance(Dimension dim) {
        int diffX = Math.abs(target.width - dim.width);
        int diffY = Math.abs(target.height - dim.height);

        return diffX + diffY;
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
