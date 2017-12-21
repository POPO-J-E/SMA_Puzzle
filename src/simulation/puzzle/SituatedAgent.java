package simulation.puzzle;

import madkit.kernel.AbstractAgent;
import madkit.kernel.Agent;
import madkit.kernel.Message;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SituatedAgent extends Agent {

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

    protected int[][] whiteGradient;
    protected int[][] targetGradient;

    protected Color color;
    protected int id;
    public boolean running = false;
    private String name;

    public SituatedAgent(Dimension location, Dimension target, int id) {
        this.location.setSize(location);
        this.target.setSize(target);
        this.id = id;
    }

    /**
     * initialize my role and fields
     */
    @Override
    protected void activate() {
        requestRole(MySimulationModel.MY_COMMUNITY, MySimulationModel.SIMU_GROUP, MySimulationModel.AGENT_ROLE);
        Dimension envDim = environment.getDimension();

        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        color = new Color(r, g, b);

        whiteGradient = new int[envDim.width][envDim.height];
        targetGradient = new int[envDim.width][envDim.height];
    }

    protected void nextStep()
    {
        setRunning(true);
    }

    @Override
    protected void live() {
        while(true)
        {
            if(isRunning())
            {
                setRunning(false);
                doLive();
            }
            else
            {
                pause(500);
            }
            pause((int)(100+Math.random()*50));
        }
    }

    protected void doLive() {
        try {
            verifPosition();
        } catch (Exception e) {
            e.printStackTrace();
        }

        AgressionMessage message = receiveMessages();

        if(message != null)
        {
            getLogger().info("fleeeee");
            flee(message);
        }
        else
        {
            if(/*!isBlocked() && */!isSatisfied())
            {
                satisfy();
            }
        }
    }

    private void verifPosition() throws Exception {
        if(environment.getAgentAt(this.location, null) != this)
        {
            throw new Exception(name+" not at goot position "+this.location+" environnement : "+environment.getAgentLocation(this));
        }
    }

    private void satisfy()
    {
        if(canBeSatisfied())
        {
            doSatisfaction();
        }
        else
        {
            satisfactionAgression();
        }
    }

    private void flee(AgressionMessage message) {
        Dimension place = null;
        int nbTry = 0;

        while(place == null && nbTry < 5)
        {
            place = findPlaceForFleeing(message.getFrom(), message.getConstraint());
            nbTry++;
        }

        if(place != null)
        {
            nbTry = 0;
            boolean flee = false;

            while(!flee && nbTry < 5)
            {
                flee = fleeAgression(place);
                nbTry++;
            }

            if(flee)
                doFlee(message, place);
        }
    }

    private AgressionMessage receiveMessages() {
        Message lastMessage = purgeMailbox();

        if(lastMessage != null && lastMessage instanceof AgressionMessage)
        {
            return (AgressionMessage)lastMessage;
        }

        return null;
    }

    private boolean isInMap(Dimension dim) {
        return dim.width >=0 && dim.height>= 0 && dim.width<environment.getDimension().width && dim.height<environment.getDimension().height;
    }


    private int calcDistance(Dimension dim) {
        int diffX = Math.abs(location.width - dim.width);
        int diffY = Math.abs(location.height - dim.height);

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

    public int getId() {
        return id;
    }

    // CONDITIONS

    private boolean isSatisfied()
    {
        return location.equals(target);
    }

    private boolean canBeSatisfied()
    {
        return isAdjacentToGoal() && patchIsEmpty(target);
    }

    private boolean isBlocked()
    {
        List<Dimension> dimensions = getAdjacentLocations();

        for (Dimension dim : dimensions) {
            if(patchIsEmpty(dim))
                return false;
        }
        return true;
    }

    private boolean isAdjacentToGoal()
    {
        return calcDistance(target) == 1;
    }

    private boolean patchIsEmpty(Dimension dim)
    {
        return environment.getAgentAt(dim, this) == null;
    }

    private List<Dimension> getAdjacentLocations()
    {
        Dimension d1 = new Dimension(location.width,location.height-1);
        Dimension d2 = new Dimension(location.width+1,location.height);
        Dimension d3 = new Dimension(location.width,location.height+1);
        Dimension d4 = new Dimension(location.width-1,location.height);

        Dimension[] dimensions = {// top, right, bottom, left
                d1,
                d2,
                d3,
                d4,
        };

        ArrayList<Dimension> sortedDimensions = new ArrayList<>();

        for (Dimension dim : dimensions)
        {
            if (isInMap(dim))
            {
                sortedDimensions.add(dim);
            }
        }

        return sortedDimensions;
    }

    // COMPORTEMENTS

    private void doSatisfaction()
    {
        moveOn(target);
    }

    private void satisfactionAgression()
    {
        Dimension location = findPlaceForSatisfaction();
        Dimension constraint = findConstraintForSatisfaction();
        tryAggressOrMove(location, constraint);
    }

    private void doFlee(AgressionMessage message, Dimension location)
    {
        if(moveOn(location));
            sendReply(message, new Message());
    }

    private boolean fleeAgression(Dimension location)
    {
        if(canFleeOn(location))
        {
            return true;
        }
        else
        {
            return aggress(location, findConstraintForFleeAgression());
        }
    }

    private boolean canFleeOn(Dimension location) {
        return canMoveOn(location);
    }

    private void tryAggressOrMove(Dimension location, Dimension constraint) {
        if(aggress(location, constraint))
            moveOn(location);
    }

    private boolean aggress(Dimension location, Dimension constraint)
    {
        if(location != null)
            return aggress(environment.getAgentAt(location, this), location, constraint);
        return false;
    }

    private boolean aggress(AbstractAgent agent, Dimension location,  Dimension constraint)
    {
        if(agent != null)
        {
            getLogger().info("agresss at position " + location + "with constraint " + constraint);
            Message ack = this.sendMessageAndWaitForReply(agent.getAgentAddressIn(MySimulationModel.MY_COMMUNITY, MySimulationModel.SIMU_GROUP, MySimulationModel.AGENT_ROLE), new AgressionMessage(this.location, constraint), 100);

            return ack != null;
        }

        return true;
    }

    private boolean canMoveOn(Dimension dimension)
    {
        if(calcDistance(dimension) <= 1 && environment.getAgentAt(dimension, this) == null)
        {
            return true;
        }
        return false;
    }

    private boolean moveOn(Dimension dimension)
    {
        if(calcDistance(dimension) > 1)
        {
            getLogger().info("!!! move to not adjacent location");
        }
        else if(environment.moveAgentTo(this, dimension))
        {
            this.location.setSize(dimension);
            return true;
        }
        return false;
    }

    // CHOIX DES DEPLACEMENTS

    private Dimension findPlaceForSatisfaction()
    {
        updateTargetGradient();

        getLogger().info("find place for satisfaction");
        getLogger().info("at : " + location.toString());
        getLogger().info(GradientHelper.gradientToString(targetGradient));

        List<Dimension> bests = GradientHelper.getBestsNear(targetGradient, getAdjacentLocations());

        if(bests.size() > 1)
        {
            updateTargetGradient();
            bests = GradientHelper.getBestsNear(whiteGradient, bests);
        }

        if(bests.size() > 0)
            return bests.get(0);

        return null;
    }

    private Dimension findPlaceForFleeing(Dimension from, Dimension constraint)
    {
        StringBuilder info = new StringBuilder();
        info.append("search place for fleeing at ").append(location).append(" attacked from ").append(from).append("\n");
        updateWhiteGradient(constraint);
        info.append(GradientHelper.gradientToString(whiteGradient)).append("\n");
        GradientHelper.setWorstChoice(whiteGradient, from);
        info.append(GradientHelper.gradientToString(whiteGradient)).append("\n");
        List<Dimension> bests = GradientHelper.getBestsNear(whiteGradient, getAdjacentLocations());

        if(bests.size() > 1)
        {
            updateTargetGradient();
            bests = GradientHelper.getBestsNear(targetGradient, bests);
            GradientHelper.setWorstChoice(targetGradient, from);
        }

        info.append("find : ").append(bests.size() > 0 ? bests.get(0) : null).append("\n");
        getLogger().info(info.toString());

        if(bests.size() > 0)
            return bests.get(0);

        return null;
    }

    private void updateTargetGradient()
    {
        GradientHelper.initGradient(targetGradient);
        GradientHelper.calcGradient(targetGradient, target);
    }

    private void updateWhiteGradient()
    {
        GradientHelper.initGradient(whiteGradient);

        List<Dimension> locs = environment.getWhiteLocs();
        for(int i =0; i < locs.size(); i++)
        {
            GradientHelper.calcGradient(whiteGradient, locs.get(i));
        }
    }

    private void updateWhiteGradient(Dimension constraint)
    {
        updateWhiteGradient();
        whiteGradient[(int)constraint.getWidth()][(int)constraint.getHeight()] = -1;
    }

    private Dimension findConstraintForFleeAgression()
    {
        return new Dimension(this.getLocation());
    }

    private Dimension findConstraintForSatisfaction()
    {
        return new Dimension(target);
    }

    public synchronized boolean isRunning() {
        return running;
    }

    public synchronized void setRunning(boolean running) {
        this.running = running;
    }

    public String getName() {
        if(this.name == null) {
            this.name = this.getClass().getSimpleName() + "-" + this.id;
        }

        return this.name;
    }
}