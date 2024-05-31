package ninetyNinePercentChain.Network;
import java.util.ArrayList;

import ninetyNinePercentChain.Network.InterNode.Sync.SyncChain;
import ninetyNinePercentChain.Network.InterNode.Write.NetworkSendManager;

public class NodeIP {
	private static ArrayList<String> nodeIPs=new ArrayList<String>();
	private static int currentIndex=0;
	/*
	Name: addIP
	Description: Checks if the list of IP addresses contains the ip. If it doesn't, we add the ip to the list, and update the NetworkSendManager and sync our version of the blockchain with them.
	Precondition: None
	Postcondition: IP added to list if not already there and new SyncChain thread started
	*/
	public static void addIP(String ip) {
		if(!nodeIPs.contains(ip)) {
			nodeIPs.add(ip);
			NetworkSendManager.update();
			new SyncChain(ip);
		}
	}
	/*
	Name: removeIP
	Description: Removes the IP from our list and updates the NetworkSendManager
	Precondition: None
	Postcondition: IP removed, if present
	*/
	public static void removeIP(String ip) {
		nodeIPs.remove(ip);
		NetworkSendManager.update();
	}
	/*
	Name: getIP
	Description: Gets the ip at index index
	Precondition: There is an index index
	Postcondition: The IP at index index is returned
	*/
	public static String getIP(int index) {
		return nodeIPs.get(index);
	}
	/*
	Name: getSize
	Description: Gets the size of the nodeIPs array
	Precondition: None
	Postcondition: nodeIPs.size returned
	*/
	public static int getSize() {
		return nodeIPs.size();
	}
	/*
	Name: getNextIP
	Description: Keeps a rolling index. This lets us loop through the ip addresses. For the DNS server, this allows us to make sure that we are distributing all our ips equally. If not, we might have 100 ips, but we only ever share the first 10.
	Precondition: None
	Postcondition: Returns the next ip in the list
	*/
	public static String getNextIP() {
		currentIndex=currentIndex++%nodeIPs.size();
		return getIP(currentIndex);
	}
}