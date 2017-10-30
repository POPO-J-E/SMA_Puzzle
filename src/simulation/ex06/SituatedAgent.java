package simulation.ex06;

import madkit.kernel.AbstractAgent;
import madkit.kernel.Message;
import simulation.puzzle.DistanceDimension;
import simulation.puzzle.LocationMessage;

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
    protected float ratio;
    private static Random rand = new Random();
    private int numero;

    public SituatedAgent(Dimension location, Dimension target, float ratio, int numero) {
        this.location = location;
        this.target = target;
        this.ratio = ratio;
        this.numero = numero;
    }

    /**
     * initialize my role and fields
     */
    @Override
    protected void activate() {
        requestRole(MySimulationModel.MY_COMMUNITY, MySimulationModel.SIMU_GROUP, MySimulationModel.AGENT_ROLE);
//        Dimension envDim = environment.getDimension();
//
//        location.width = (int) (Math.random() * envDim.width);
//        location.height = (int) (Math.random() * envDim.height);
//
//        target.width = (int) (Math.random() * envDim.width);
//        target.height = (int) (Math.random() * envDim.height);

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
        boolean needToMove = false;

        if (!isMessageBoxEmpty())
        {
            Message message = nextMessage();
            System.out.println(message);
            if (ratio < rand.nextFloat())
            {
                needToMove = true;
            }
            else
            {
                needToMove = false;
            }
        }
        if (calcDistance(location)==0 && !needToMove)
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

        int size = sortedDistanceDimensions.size();
        int i = 0;
        AbstractAgent agentToContact = null;
        Dimension locToGo = null;
        for (int j=0; j<sortedDistanceDimensions.size(); j++){
            Dimension newLoc = sortedDistanceDimensions.get(j).getDimension();

            AbstractAgent agent = environment.getAgentAt(newLoc, this);

            if(agent == null)
            {
                if((newLoc.width != previousLocation.width && newLoc.height != previousLocation.height) /*|| i == size-1*/)
                {
                    previousLocation.width = location.width;
                    previousLocation.height = location.height;

                    location.width = newLoc.width;
                    location.height = newLoc.height;
                    return;
                }
            }
            else if (agentToContact == null){
                agentToContact = agent;
                locToGo = newLoc;
            }
            i++;
        }
        if (agentToContact != null)
            sendMessage(agentToContact.getAgentAddressIn(MySimulationModel.MY_COMMUNITY, MySimulationModel.SIMU_GROUP, MySimulationModel.AGENT_ROLE), new LocationMessage(locToGo));

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

    public int getNumero() {
        return numero;
    }

    @Override
    public String toString() {
        return "SituatedAgent{" +
                "numero=" + numero +
                '}';
    }
}
