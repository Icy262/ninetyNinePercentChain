package ninetyNinePercentChain.Network.DNS;
import java.net.Socket;

import ninetyNinePercentChain.Network.NodeIP;

import java.lang.Thread;
import java.io.ObjectInputStream;

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
		while(continueThread) {
			try {
				Socket socket=new Socket(DNSIP, port);
				ObjectInputStream read=new ObjectInputStream(socket.getInputStream());
				for(int i=0; i<10; i++) {
					NodeIP.addIP((String) read.readObject());
				}
				read.close();
				socket.close();
				if(NodeIP.getSize()==0) {
					Thread.sleep(1000);
				} else {
					Thread.sleep(600000);
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