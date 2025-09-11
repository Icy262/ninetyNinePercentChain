package ninetyNinePercentChain.NetworkTransaction;

import ninetyNinePercentChain.Network.DNS.QueryDNS;
import ninetyNinePercentChain.Network.DNS.RegisterDNS;
import ninetyNinePercentChain.Network.InterNode.Read.NetworkReceiveHandler;
import ninetyNinePercentChain.Network.InterNode.Sync.SyncChainResponseManager;
import ninetyNinePercentChain.Network.InterNode.Write.NetworkSendManager;
import ninetyNinePercentChain.Network.KeepAlive.KeepAliveManager;
import ninetyNinePercentChain.Network.KeepAlive.KeepAliveResponse;

/*
 * This is used instead of Main when we are running our code as a submodule of a bigger project. It basically does the same thing as main does.
 */

public class NetworkInterface {
	private static QueryDNS queryDNS;
	private static KeepAliveResponse keepAliveResponse;
	private static KeepAliveManager keepAliveManager;
	private static NetworkReceiveHandler networkReceiveHandler;
	private static SyncChainResponseManager syncChainResponseManager;
	/*
	Name: setup
	Description: When we are running a non hashing node, that will be used to create and recieve transactions only, as part of a larger project, this will be used instead of main. Initializes the class to have all the connections and threads necessary.
	Precondition: None
	Postcondition: Program is ready to create and process transactions
	*/
	public static void setup() {
		queryDNS=new QueryDNS(); //Creates a QueryDNS object
		keepAliveResponse=new KeepAliveResponse(); //Creates a KeepAliveResponse object
		keepAliveManager=new KeepAliveManager(); //Creates a KeepAliveManager object
		networkReceiveHandler=new NetworkReceiveHandler(false); //Creates a NetworkRecieveHandler object with hashing flag set to false. This will make it so that this node does not process transactions, but instead it redirects them to the WaitForTransactionManager
		syncChainResponseManager=new SyncChainResponseManager(); //Creates a SyncChainResponseManager object
		new RegisterDNS().start(); //Starts the RegisterDNS thread. This will tell the DNS server that we are a node and we are online.
		queryDNS.start(); //Starts the QueryDNS thread. This will query the DNS server for new IP adresses every 10 minutes or if there was no IPs returned last time, 1 second.
		keepAliveResponse.start(); //Starts the KeepAliveResponse thread. This will respond to any incoming keep alive requests by starting a new KeepAliveResponse thread.
		keepAliveManager.start(); //Starts the KeepAliveManager thread. This will send out keep alive messages to all the IPs in our list every five minutes. If they don't accept the connection within 10 seconds, we delete their ip from our list.
		networkReceiveHandler.start(); //Starts the NetworkRecieveHandler thread with the hashing flag set to true. This will accept any incoming data and direct it to the hashing manager.
		syncChainResponseManager.start(); //Starts the SyncChainReponseManager. This will sync our copy of the blockchain with any new IPs that we add to our list.
	}
	/*
	Name: cleanup
	Description: Stops everything and cleans up after the setup method. To be used when stopping the program
	Precondition: Setup has already been called. Cleanup has not been called yet
	Postcondition: Threads stopped and cleanup finished
	*/
	public static void cleanup() {
		queryDNS.stopThread(); //Stops the queryDNS thread
		keepAliveResponse.stopThread(); //Stops the keepAliveResponse thread
		keepAliveManager.stopThread(); //Stops the keepAliveManager thread
		networkReceiveHandler.stopThread(); //Stops the networkReceiveHandler thread
		syncChainResponseManager.stopThread(); //Stops the syncChainRepsponseManager
		NetworkSendManager.stopThreads(); //Stops all write threads
	}
}