package communication.ex01;

import static communication.ex01.Society.COMMUNITY;
import static communication.ex01.Society.GROUP;
import static communication.ex01.Society.ROLE;

import java.util.logging.Level;

import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Message;

/**
 * Shows how agents exchange messages.
 * 
 * 
 * 
 * 		#jws communication.ex01.PingAgent jws#
 * 
 * 
 * 
 * To exchange messages, agents have to exist in an artificial society. That is, agents have to create communities
 * containing groups and take roles within to interact with others. Doing so, agents get agent addresses which could be
 * used to send them messages. Here, two agents take a role and ping the other one.
 */
public class PingAgent extends Agent {

    /**
     * Firstly, take a position in the artificial society
     */
    @Override
    protected void activate() {
	// Thanks to this log level, everything the agent does in the artificial
	// society will be automatically logged
	getLogger().setLevel(Level.FINEST);

	createGroup(COMMUNITY, GROUP);
	requestRole(COMMUNITY, GROUP, ROLE);

	pause(500);
    }

    /**
     * Now ping the other agent with a message
     */
    @Override
    protected void live() {

	// now getting the address of a partner using getAgentWithRole
	AgentAddress other = null;
	while (other == null) {
	    // This way, I wait for another coming into play
	    other = getAgentWithRole(COMMUNITY, GROUP, ROLE);
	    pause(1000);
	}

	getLogger().info("\n\tI found someone !!\n" + other + "\n\n");
	pause(1000);

	// sending the message to other: Success will be logged
	sendMessage(other, new Message());
	pause(1000);

	// waiting other's message: The reception will be logged
	waitNextMessage();
	pause(10000);
    }

    /**
     * @param args
     */
    public static void main(String[] argss) {
	executeThisAgent(2, true);
	// or
	// String[] args = { "--launchAgents", PingAgent.class.getName() + ",true,2" };
	// Madkit.main(args);
    }

}
