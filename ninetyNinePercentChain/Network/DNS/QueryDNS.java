package ninetyNinePercentChain.Network.DNS;
import java.net.Socket;

import ninetyNinePercentChain.Network.NodeIP;

import java.lang.Thread;
import java.io.ObjectInputStream;

/*
 * Connects to the DNS server and gets 10 IP addresses from it. It adds these to our list of IPs. It repeats this every 10 minutes while the program is running. If we don't have any IP addresses, we repeat this every 1 second instead, until we get an IP.
 */

public class QueryDNS extends Thread {
	private int port=9940;
	private String DNSIP="10.10.166.222";
	private boolean continueThread=true;
	/*
	Name: run
	Description: Opens a Socket to the DNS Seed server. Reads 10 IP addresses from the socket and adds them to the NodeIP list. Closes the socket and waits. If there were no IPs given, wait one second before trying again. If not, wait ten minutes.
	Precondition: None
	Postcondition: IP addresses are added to the NodeIP list.
	*/
	public void run() {
		while(continueThread) { //While the continueRunning flag is set to true,
			try {
				Socket socket=new Socket(DNSIP, port); //Create a new connection to the DNS server
				ObjectInputStream read=new ObjectInputStream(socket.getInputStream()); //Open a ObjectInputStream with the server
				for(int i=0; i<10; i++) { //Repeat 10 times
					NodeIP.addIP((String) read.readObject()); //Read objects from the DNS server and convert them to Strings. Store the Strings in the NodeIP IP list.
				}
				read.close(); //Close the reader to prevent resource leaks
				socket.close(); //Close the socket to save bandwith and resources
				if(NodeIP.getSize()==0) { //If we don't have any other node's IPs,
					Thread.sleep(1000); //Wait one second and try again. We wait for a second to prevent excessive network traffic.
				} else { //We have some IPs,
					Thread.sleep(600000); //Check in again in 10 minutes
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/*
	Name: stopThread
	Description: Sets a flag to false. This causes the thread to stop.
	Precondition: None
	Postcondition: Thread is stopped, if running
	*/
	public void stopThread() {
		continueThread=false;
	}
}