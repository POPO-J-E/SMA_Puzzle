package simulation.ex06;

import madkit.kernel.AbstractAgent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 
 * 
 * #jws simulation.ex06.MySimulationModel jws#
 * 
 * It is time to display something !! The only purpose of this class is to show an example of what could be a launching
 * sequence. The display work is done in {@link Viewer}
 */
public class MySimulationModel extends AbstractAgent {

    // Organizational constants
    public static final String MY_COMMUNITY = "simu";
    public static final String SIMU_GROUP = "simu";
    public static final String AGENT_ROLE = "agent";
    public static final String ENV_ROLE = "environment";
    public static final String SCH_ROLE = "scheduler";
    public static final String VIEWER_ROLE = "viewer";
    public static final int AGENT_NUMBER = 5;

    @Override
    protected void activate() {
        // 1 : create the simulation group
        createGroup(MY_COMMUNITY, SIMU_GROUP);

        // 2 : create the environment
        EnvironmentAgent environment = new EnvironmentAgent();
        launchAgent(environment);

        List<Dimension> starts = new ArrayList<>(AGENT_NUMBER);
        List<Dimension> targets = new ArrayList<>(AGENT_NUMBER);

        fillList(starts,environment.getDimension());
        fillList(targets,environment.getDimension());

        Random rand = new Random();

        // 4 : launch some simulated agents
        for (int i = 0; i < AGENT_NUMBER; i++) {
            int pos = rand.nextInt(starts.size()-1);
            int pos2 = rand.nextInt(starts.size()-1);
            AbstractAgent agent1 = new SituatedAgent(starts.get(pos),targets.get(pos2), rand.nextFloat(),i+1);
            starts.remove(pos);
            targets.remove(pos2);
            launchAgent(agent1);
        }

        // 5 : create the scheduler
        MyScheduler06 scheduler = new MyScheduler06();
        launchAgent(scheduler, true);

        // 3 : create the viewer
        Viewer viewer = new Viewer();
        launchAgent(viewer, true);
	
    }

    private void fillList(List<Dimension> starts, Dimension dimension) {
        for (int i=0; i<dimension.getWidth(); i++){
            for (int j=0; j<dimension.getHeight(); j++) {
                starts.add(new Dimension(i,j));
            }
        }
    }

    public static void main(String[] args) {
	executeThisAgent(1, false); // no gui for me
    }
}
