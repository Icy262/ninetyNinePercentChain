package ninetyNinePercentChain.Network.KeepAlive;

import ninetyNinePercentChain.Network.NodeIP;

/*
 * Every five minutes, we go through the list of IPs and make keep alive connections.
 */

public class KeepAliveManager extends Thread {
	private boolean continueRunning=true; //Flag to control if the program should continue running
	/*
	Name: run
	Description: Every 5 minutes, send a keep alive to each of the IPs in the IP list
	Precondition: None
	Postcondition: New threads started for each IP in the IP list every 5 minutes
	*/
	public void run() {
		while(continueRunning) { //While the thread should continue running
			try {
				for(int i=0; i<NodeIP.getSize(); i++) { //For each IP in our list,
					new KeepAlive(NodeIP.getIP(i)).start(); //Create a new KeepAlive thread to check if it is still online
				}
				Thread.sleep(300000); //Waits for 5 minutes
			} catch(Exception e) {
				System.out.println(e);
			}
		}
	}
	/*
	Name: stopThread
	Description: Sets the continueRunning flag to false. This stops the thread.
	Precondition: None
	Postcondition: Thread stopped
	*/
	public void stopThread() {
		continueRunning=false;
	}
}
