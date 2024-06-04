package ninetyNinePercentChain;
import ninetyNinePercentChain.Network.DNS.QueryDNS;
import ninetyNinePercentChain.Network.DNS.RegisterDNS;
import ninetyNinePercentChain.Network.InterNode.Read.NetworkReceiveHandler;
import ninetyNinePercentChain.Network.InterNode.Sync.SyncChainResponseManager;
import ninetyNinePercentChain.Network.KeepAlive.KeepAliveManager;
import ninetyNinePercentChain.Network.KeepAlive.KeepAliveResponse;

public class Main {
	public static void main(String[] args) {
		new RegisterDNS().start(); //Starts RegisterDNS thread. This will tell the DNS server that we are a node and we are online
		new QueryDNS().start(); //Starts the QueryDNS thread. This will query the DNS server for new IP adresses every 10 minutes or if there was no IPs returned last time, 1 second.
		new KeepAliveResponse().start(); //Starts the KeepAliveResponse thread. This will respond to any incoming keep alive requests by starting a new KeepAliveResponse thread.
		new KeepAliveManager().start(); //Starts the KeepAliveManager thread. This will send out keep alive messages to all the IPs in our list every five minutes. If they don't accept the connection within 10 seconds, we delete their ip from our list.
		new NetworkReceiveHandler(true).start(); //Starts the NetworkRecieveHandler thread with the hashing flag set to true. This will accept any incoming data and direct it to the hashing manager.
		new SyncChainResponseManager().start(); //Starts the SyncChainReponseManager. This will sync our copy of the blockchain with any new IPs that we add to our list.
	}
}