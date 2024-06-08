package ninetyNinePercentChain.Network.InterNode.Write;
import java.util.ArrayList;

import ninetyNinePercentChain.Network.NodeIP;

/*
 * NetworkSendManager holds a list of NetworkSends. This class allows us to make a single function call and have an object to be sent to all other connected nodes. This manager automatically manages the list of NetworkSends so that we will never have connections with IPs not in our list. Any IP in our list will have a NetworkSend connection. When an object is passed to this method, it passes it to all the NetworkSends contained within the list. 
 */

public class NetworkSendManager {
	private static ArrayList<NetworkSend> networkSends=new ArrayList<NetworkSend>(); //List of all the connections to other nodes.
	/*
	Name: update
	Description: Checks if each IP address coresponds to a IP address in the networkSends list. If there is already a network send for the ip, we skip this ip. If not, we create a new NetworkSend and add it to the list. We also check that each NetworkSend in networkSends's ip is in NodeIP. If it is not there, we remove the NetworkSend.  
	Precondition: None
	Postcondition: All IPs in NodeIP have a connection in networkSends
	*/
	public static void update() {
		for(int i=0; i<NodeIP.getSize(); i++) { //For each IP in NodeIP,
			boolean exists=false; //Tracks if we have a connection for this IP in NodeIP's list
			for(int ii=0; ii<networkSends.size(); ii++) { //For each NetworkSend object,
				if(networkSends.get(ii).getIP().equalsIgnoreCase(NodeIP.getIP(i))) { //If the current NetworkSend's IP matches the current IP in NodeIP's list, 
					exists=true; //We have a connection
					break; //No further checks needed
				}
			}
			if(!exists){ //If the IP does not have a connection,
				networkSends.add(new NetworkSend(NodeIP.getIP(i))); //Creates a new NetworkSend object with the ip
				networkSends.get(networkSends.size()-1).start(); //Starts the new thread
			}
		}
		for(int i=0; i<networkSends.size(); i++) { //For each IP in networkSends,
			boolean foundIP=false; //Default flag to false
			for(int ii=0; ii<NodeIP.getSize(); ii++) { //For each IP in NodeIP,
				if(networkSends.get(i).getIP().equalsIgnoreCase(NodeIP.getIP(ii))) { //If the IP of the NetworkSend and the IP we are searching for are the same,
					foundIP=true; //Sets foundIP flag to true. This prevents the IP from being removed
					break; //No more checks needed
				}
			}
			if(!foundIP) { //If we didn't find the ip,
				networkSends.remove(i); //We should remove the ip because it is no longer active
			}
		}
	}
	/*
	Name: addToQueue
	Description: Adds a new object to the send queue. The object is distributed to each of the sockets
	Precondition: Object is not null
	Postcondition: Object is sent to all the endpoints in networkSends
	*/
	public static void addToQueue(Object toSend) {
		for(int i=0; i<networkSends.size(); i++) { //For each connection we have,
			networkSends.get(i).addToQueue(toSend); //Add the object to the queue of objects to send
		}
	}
	/*
	Name: stopThreads
	Description: Stops all the threads managed by the NetworkSendManager
	Precondition: None
	Postcondition: All threads stopped
	*/
	public static void stopThreads() {
		for(int i=0; i<networkSends.size(); i++) { //For each connection we have,
			networkSends.get(i).stopThread(); //Tells the connection to stop it's thread
		}
	}
}