package simulation.puzzle;

import madkit.kernel.AbstractAgent;
import madkit.kernel.Watcher;
import madkit.simulation.probe.PropertyProbe;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This agent is used to model a quite simple environment. Nothing in it; It just defines its boundaries and uses a
 * {@link PropertyProbe} to set the agents' environment field so that they can use the environment's methods once they
 * enter the artificial society.
 */
public class EnvironmentAgent extends Watcher {

    /**
     * environment's boundaries
     */
    private Dimension dimension;

    private List<AbstractAgent> agents;
    private SituatedAgent[][] map;
	private List<Dimension> whiteLocs;

	/**
     * so that the agents can perceive my dimension
     */
    public Dimension getDimension() {
	return dimension;
    }

	public EnvironmentAgent(Dimension dimension) {
		this.dimension = dimension;
	}

	public EnvironmentAgent(int width, int height) {
		this(new Dimension(width, height));
	}

	@Override
    protected void activate() {
		agents = new ArrayList<>();
		map = new SituatedAgent[(int)dimension.getWidth()][(int)dimension.getHeight()];

		// 1 : request my role so that the viewer can probe me
		requestRole(MySimulationModel.MY_COMMUNITY, MySimulationModel.SIMU_GROUP, MySimulationModel.ENV_ROLE);

		// 2 : this probe is used to initialize the agents' environment field
		addProbe(new AgentsProbe(MySimulationModel.MY_COMMUNITY, MySimulationModel.SIMU_GROUP, MySimulationModel.AGENT_ROLE, "environment"));

   }

	public synchronized List<Dimension> getWhiteLocs() {
    	List<Dimension> whites = new ArrayList<>();

		for(int i = 0; i < dimension.getWidth(); i++)
		{
			for(int j = 0; j < dimension.getHeight(); j++)
			{
				Dimension loc = new Dimension(i, j);
				if(getAgentAt(loc, null) == null)
				{
					whites.add(loc);
				}
			}
		}

		return whites;
	}

	public void initWhites() {
    	whiteLocs = new ArrayList<>();

    	for(int i = 0; i < dimension.getWidth(); i++)
		{
			for(int j = 0; j < dimension.getHeight(); j++)
			{
				Dimension loc = new Dimension(i, j);
				if(getAgentAt(loc, null) == null)
				{
					whiteLocs.add(loc);
				}
			}
		}
	}

	public Dimension getAgentLocation(SituatedAgent situatedAgent) {
		for(int i = 0; i < dimension.getWidth(); i++)
		{
			for(int j = 0; j < dimension.getHeight(); j++)
			{
				Dimension loc = new Dimension(i, j);
				if(getAgentAt(loc, null) == situatedAgent)
				{
					return loc;
				}
			}
		}
		return null;
	}

	class AgentsProbe extends PropertyProbe<AbstractAgent, EnvironmentAgent> {

		public AgentsProbe(String community, String group, String role, String fieldName) {
			super(community, group, role, fieldName);
		}

		@Override
		protected void adding(AbstractAgent agent) {
			super.adding(agent);
			setPropertyValue(agent, EnvironmentAgent.this);
			agents.add(agent);

			if(agent instanceof SituatedAgent)
			{
				SituatedAgent situatedAgent = (SituatedAgent)agent;
				Dimension dimension = situatedAgent.getLocation();
				setAgentAt(situatedAgent, dimension);
			}
		}
    }

    public synchronized SituatedAgent getAgentAt(Dimension location, AbstractAgent agentAsker)
	{
		SituatedAgent agent = map[(int)location.getWidth()][(int)location.getHeight()];
		if(agent == null || agent == agentAsker)
			return null;

		return agent;
	}

	public synchronized boolean moveAgentTo(SituatedAgent agent, Dimension location)
	{
		if(getAgentAt(location, agent) == null)
		{
			whiteLocs.remove(new Dimension(location));
			whiteLocs.add(agent.getLocation());
			setAgentAt(null, agent.getLocation());
			setAgentAt(agent, location);

			return true;
		}
		else
		{
			return false;
		}
	}

	private void setAgentAt(SituatedAgent agent, Dimension location)
	{
		map[(int)location.getWidth()][(int)location.getHeight()] = agent;
	}
}