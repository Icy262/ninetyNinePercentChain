package ninetyNinePercentChain;

import java.util.Scanner;
import java.lang.InterruptedException;

import ninetyNinePercentChain.Network.DNS.QueryDNS;
import ninetyNinePercentChain.Network.DNS.RegisterDNS;
import ninetyNinePercentChain.Network.InterNode.Read.NetworkReceiveHandler;
import ninetyNinePercentChain.Network.InterNode.Sync.SyncChainResponseManager;
import ninetyNinePercentChain.Network.KeepAlive.KeepAliveManager;
import ninetyNinePercentChain.Network.KeepAlive.KeepAliveResponse;
import ninetyNinePercentChain.Network.InterNode.Write.NetworkSendManager;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		QueryDNS queryDNS=new QueryDNS(); //Creates a QueryDNS object
		KeepAliveResponse keepAliveResponse=new KeepAliveResponse(); //Creates a KeepAliveResponse object
		KeepAliveManager keepAliveManager=new KeepAliveManager(); //Creates a KeepAliveManager object
		NetworkReceiveHandler networkReceiveHandler=new NetworkReceiveHandler(true); //Creates a NetworkRecieveHandler object with hashing flag set to true
		SyncChainResponseManager syncChainResponseManager=new SyncChainResponseManager(); //Creates a SyncChainResponseManager object
		
		new RegisterDNS().start(); //Starts the RegisterDNS thread. This will tell the DNS server that we are a node and we are online.
		queryDNS.start(); //Starts the QueryDNS thread. This will query the DNS server for new IP adresses every 10 minutes or if there was no IPs returned last time, 1 second.
		keepAliveResponse.start(); //Starts the KeepAliveResponse thread. This will respond to any incoming keep alive requests by starting a new KeepAliveResponse thread.
		keepAliveManager.start(); //Starts the KeepAliveManager thread. This will send out keep alive messages to all the IPs in our list every five minutes. If they don't accept the connection within 10 seconds, we delete their ip from our list.
		networkReceiveHandler.start(); //Starts the NetworkRecieveHandler thread with the hashing flag set to true. This will accept any incoming data and direct it to the hashing manager.
		syncChainResponseManager.start(); //Starts the SyncChainReponseManager. This will sync our copy of the blockchain with any new IPs that we add to our list.
		Scanner userInput=new Scanner(System.in); //Scanner.
		boolean stopProgram=false; //Flag. If false, program continues running. If true, program stops.
		while(!stopProgram) { //While stopProgram is false.
			System.out.println("Type \"STOP\" to stop the program");
			if(userInput.nextLine().equalsIgnoreCase("STOP")); { //If user enters "STOP",
				stopProgram=true; //Sets the stopProgram flag to true. This will stop the program
				userInput.close(); //Closes the Scanner
				queryDNS.stopThread(); //Stops the queryDNS thread
				keepAliveResponse.stopThread(); //Stops the keepAliveResponse thread
				keepAliveManager.stopThread(); //Stops the keepAliveManager thread
				networkReceiveHandler.stopThread(); //Stops the networkReceiveHandler thread
				syncChainResponseManager.stopThread(); //Stops the syncChainRepsponseManager
				NetworkSendManager.stopThreads(); //Stops all write threads
				Thread.sleep(10000); //Gives a 10 second delay to ensure all thread cleanup is done
				System.exit(0); //Stops the entire application. All of our stuff is cleaned up. All that is running now is this thread and the java vm threadss
			}
		}
	}
}