package ninetyNinePercentChain.Network;
import java.util.ArrayList;

import ninetyNinePercentChain.Network.InterNode.Sync.SyncChain;
import ninetyNinePercentChain.Network.InterNode.Write.NetworkSendManager;

/*
 * Holds a list of all the other node's IP addresses that we know about. When we add or remove an ip, we update the network send manager. When we add a new ip, we sync our version of the chain with them. Adding an IP will never produce a duplicate copy of the IP; IPs will only be added to the list if the IP does not already exist there. Removing an IP only works if the IP is there, if not nothing happens. This makes interacting with this class very simple as any ips can be added or removed without concern.
 */

public class NodeIP {
	private static ArrayList<String> nodeIPs=new ArrayList<String>(); //List of other node's IP addresses
	private static int currentIndex=0; //Tracks our current position in the array list.
	private static String localIP; //Tracks our local ip. This prevents accidental looping
	/*
	Name: addIP
	Description: Checks if the list of IP addresses contains the ip. If it doesn't, we add the ip to the list, and update the NetworkSendManager and sync our version of the blockchain with them.
	Precondition: None
	Postcondition: IP added to list if not already there and new SyncChain thread started
	*/
	public static void addIP(String ip) {
		if(!nodeIPs.contains(ip)&&!ip.equalsIgnoreCase(localIP)) { //If the IP is not already in our list and it isn't our own IP,
			nodeIPs.add(ip); //Add the IP to our list
			NetworkSendManager.update(); //Update the NetworkSendManager so that it will send our Blocks and Transactions to the new IP
			new SyncChain(ip); //Sync our version of the block chain with the other node
		}
	}
	/*
	Name: removeIP
	Description: Removes the IP from our list and updates the NetworkSendManager
	Precondition: None
	Postcondition: IP removed, if present
	*/
	public static void removeIP(String ip) {
		nodeIPs.remove(ip); //Removes the IP from our list
		NetworkSendManager.update(); //Updates the NetworkSendManager so that it will remove the IP
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
		currentIndex=currentIndex++%nodeIPs.size(); //Increments the index. If the index is past the end of the list, loop back around to the start.
		return getIP(currentIndex); //Returns the next IP
	}
	/*
	Name: setLocalIP
	Description: Sets the value of newLocalIP. We use this to prevent accidentally sending stuff to ourself.
	Precondition: newLocalIP is our ip address
	Postcondition: localIP set to the value of newLocalIP
	*/
	public static void setLocalIP(String newLocalIP) {
		localIP=newLocalIP;
	}
}