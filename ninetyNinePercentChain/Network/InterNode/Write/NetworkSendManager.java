package ninetyNinePercentChain.Network.InterNode.Write;
import java.util.ArrayList;

import ninetyNinePercentChain.Network.NodeIP;

public class NetworkSendManager {
	private static ArrayList<NetworkSend> networkSends;
	/*
	Name: update
	Description: Checks if each IP address coresponds to a IP address in the networkSends list. If there is already a network send for the ip, we skip this ip. If not, we create a new NetworkSend and add it to the list. We also check that each NetworkSend in networkSends's ip is in NodeIP. If it is not there, we remove the NetworkSend.  
	Precondition: None
	Postcondition: All IPs in NodeIP have a connection in networkSends
	*/
	public static void update() {
		for(int i=0; i<NodeIP.getSize(); i++) { //For each IP in NodeIP
			for(int ii=0; ii<networkSends.size(); ii++) { //Compare the current IP in NodeIP to each of the IPs in networkSends
				if(networkSends.get(ii).getIP().equalsIgnoreCase(NodeIP.getIP(i))) { //IP in NodeIP is already in networkSends
					break;
				}
			}
			networkSends.add(new NetworkSend(NodeIP.getIP(i)));
		}
		for(int i=0; i<networkSends.size(); i++) { //For each IP in networkSends
			boolean foundIP=false;
			for(int ii=0; ii<NodeIP.getSize(); ii++) { //Compare the current IP with each IP in NodeIP
				if(networkSends.get(i).getIP().equalsIgnoreCase(NodeIP.getIP(ii))) {
					foundIP=true;
					break;
				}
			}
			if(!foundIP) {
				networkSends.remove(i); //If we can't find the IP in networkSends in NodeIP, we should remove the ip because it is no longer active
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
		for(int i=0; i<networkSends.size(); i++) {
			networkSends.get(i).addToQueue(toSend);
		}
	}
}