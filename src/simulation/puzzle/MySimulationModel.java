package simulation.puzzle;

import madkit.kernel.AbstractAgent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 
 * 
 * #jws simulation.puzzle.MySimulationModel jws#
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
    public static final int AGENT_NUMBER = 92;
    public static final int WIDTH = 10;
    public static final int HEIGHT = 10;

    @Override
    protected void activate() {
        // 1 : create the simulation group
        createGroup(MY_COMMUNITY, SIMU_GROUP);

        // 2 : create the environment
        EnvironmentAgent environment = new EnvironmentAgent(WIDTH, HEIGHT);
        launchAgent(environment);

        List<Dimension> starts = new ArrayList<>(WIDTH*HEIGHT);
        List<Dimension> targets = new ArrayList<>(WIDTH*HEIGHT);
        List<Integer> agents = new ArrayList<>(WIDTH*HEIGHT);

        fillList(starts,environment.getDimension());
        fillList(targets,environment.getDimension());
        fillAgentList(agents,environment.getDimension());

        Random rand = new Random();

        // 4 : launch some simulated agents
        for (int i = 0; i < AGENT_NUMBER; i++) {
            int start = rand.nextInt(starts.size()-1);
            int agentNumber = rand.nextInt(agents.size()-1);
            int target = agents.remove(agentNumber);
            AbstractAgent agent1 = new SituatedAgent(starts.remove(start), targets.get(target), target+1);
            launchAgent(agent1);
        }
        environment.initWhites();

        // 5 : create the scheduler
        MyScheduler scheduler = new MyScheduler();
        launchAgent(scheduler, true);

        // 3 : create the viewer
        Viewer viewer = new Viewer();
        launchAgent(viewer, true);
    }

    private void fillList(List<Dimension> dims, Dimension dimension) {
        for (int j=0; j<dimension.getHeight(); j++) {
            for (int i=0; i<dimension.getWidth(); i++){
                dims.add(new Dimension(i,j));
            }
        }
    }

    private void fillAgentList(List<Integer> agents, Dimension dimension) {
        for (int i=0; i<dimension.width*dimension.height; i++){
            agents.add(i);
        }
    }

    public static void main(String[] args) {
	executeThisAgent(1, false); // no gui for me
    }
}
