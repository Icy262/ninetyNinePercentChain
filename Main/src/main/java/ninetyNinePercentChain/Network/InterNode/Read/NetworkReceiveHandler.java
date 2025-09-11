package ninetyNinePercentChain.Network.InterNode.Read;

import java.util.ArrayList;

import java.lang.Thread;
import java.net.ServerSocket;

/*
 * NetworkRecieveHandler listens for incoming connections. When it recieves one, it accepts and creates a new thread to handle it. NetworkRecieveHandler keeps a list of all running threads so that it can stop them when the program is shutting down
 */

public class NetworkReceiveHandler extends Thread {
	private boolean continueRunning=true;
	private ArrayList<NetworkReceive> networkReceiver=new ArrayList<NetworkReceive>();
	/*
	Name: run
	Description: Listens on port 9938 for incoming sockets. Whenever we recieve an incoming socket, we open a new socket conection in a new thread to handle it.
	Precondition: None
	Postcondition: None
	*/
	public void run() {
		try(ServerSocket endpoint=new ServerSocket(9938)) { //Creates a ServerSocket to listen for incoming connections
			while(continueRunning) { //While we should continue running,
				networkReceiver.add(new NetworkReceive(endpoint.accept())); //Accepts any incoming connections
				networkReceiver.get(networkReceiver.size()-1).start(); //Starts a new thread to handle the connection
			}
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	/*
	Name: NetworkRecieveHandler
	Description: Lets us set the NetworkRead hashing flag
	Precondition: None
	Postcondition: NetworkRead hashing flag set to the value of hashing
	*/
	public NetworkReceiveHandler(boolean hashing) {
		NetworkRead.setHashing(hashing); //Sets the hashing flag for NetworkRead 
	}
	/*
	Name: stopThread
	Description: Sets the continueRunning flag to false
	Precondition: None
	Postcondition: continueRunning flag set to false. Thread will stop.
	*/
	public void stopThread() {
		continueRunning=false; //Sets continueRunning flag to false
		for(int i=0; i<networkReceiver.size(); i++) { //For each connection,
			networkReceiver.get(i).stopThread(); //Close the connection
		}
	}
}