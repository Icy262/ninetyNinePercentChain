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
	Precondition:
	Postcondition:
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
	Precondition:
	Postcondition:
	*/
	public static void removeIP(String ip) {
		nodeIPs.remove(ip);
		NetworkSendManager.update();
	}
	/*
	Name:
	Description:
	Precondition:
	Postcondition:
	*/
	public static String getIP(int index) {
		return nodeIPs.get(index);
	}
	/*
	Name:
	Description:
	Precondition:
	Postcondition:
	*/
	public static int getSize() {
		return nodeIPs.size();
	}
	/*
	Name:
	Description:
	Precondition:
	Postcondition:
	*/
	public static String getNextIP() {
		currentIndex=currentIndex++%nodeIPs.size();
		return getIP(currentIndex);
	}
}