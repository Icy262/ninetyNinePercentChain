package ninetyNinePercentChain.Network.InterNode.Sync;
import java.io.DataOutputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import ninetyNinePercentChain.Utils.BlockFile;

/*
 * SyncChainResponder is designed to respond to a request made by another node through SyncChain. It sends the number of blocks in our blockchain directory and then sends all of the blocks in our directory over the network. This allows us to start up new nodes or update nodes which went offline temporarily.
 */

class SyncChainResponder extends Thread {
	private Socket endpoint; //Our connection to the reciever
	/*
	Name: run
	Description: Writes the number of blocks in the blockchain directory to the endopint and then writes every block in the blockchain directory over the socket to the endpoint.
	Precondition: Socket connection to endpoint is valid
	Postcondition: None
	*/
	public void run() {
		try {
			DataOutputStream primitiveOutputStream=new DataOutputStream(endpoint.getOutputStream()); //Opens a DataOutputStream to the reciever. We use this to send the number of blocks we are transferring
			ObjectOutputStream objectOutputStream=new ObjectOutputStream(endpoint.getOutputStream()); //Opens a ObjectOutputStream to the reciever. We use this to send the blocks themselves
			primitiveOutputStream.writeInt(BlockFile.getHighestIndex()-1); //Tells the reciever how many blocks to expect. We subtract one because we are skipping block 0
			for(int i=1; i<=BlockFile.getHighestIndex(); i++) { //We skip index 0 because that one is our special orgin block and isn't technically valid.
				objectOutputStream.writeObject(BlockFile.readBlock(i)); //Reads block i and sends it over the network
			}
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	/*
	Name: SyncChainResponder
	Description: Sets the Socket connection to the endpoipnt to the connection to the endpoint
	Precondition: Socket connection is a valid socket connection to a computer running the ninetyNinePercentChain software
	Postcondition: this.endpoint set to endpoint
	*/
	public SyncChainResponder(Socket endpoint) {
		this.endpoint=endpoint; //The connection to the reciever
	}
}