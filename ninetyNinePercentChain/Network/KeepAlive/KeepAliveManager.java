package ninetyNinePercentChain.Network.KeepAlive;

import ninetyNinePercentChain.Network.NodeIP;

public class KeepAliveManager extends Thread {
	private boolean continueRunning=true;
	/*
	Name: run
	Description: Every 5 minutes, send a keep alive to each of the IPs in the IP list
	Precondition: None
	Postcondition: New threads started for each IP in the IP list every 5 minutes
	*/
	public void run() {
		while(continueRunning) {
			try {
				for(int i=0; i<NodeIP.getSize(); i++) {
					new KeepAlive(NodeIP.getIP(i)).start();
				}
				Thread.sleep(300000);
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
