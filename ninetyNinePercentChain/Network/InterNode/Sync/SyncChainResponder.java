package ninetyNinePercentChain.Network.InterNode.Sync;
import java.io.DataOutputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import ninetyNinePercentChain.Utils.BlockFile;

class SyncChainResponder extends Thread {
	private Socket endpoint;
	/*
	Name: run
	Description: Writes the number of blocks in the blockchain directory to the endopint and then writes every block in the blockchain directory over the socket to the endpoint.
	Precondition: Socket connection to endpoint is valid
	Postcondition: None
	*/
	public void run() {
		try {
			DataOutputStream primitiveOutputStream=new DataOutputStream(endpoint.getOutputStream());
			ObjectOutputStream objectOutputStream=new ObjectOutputStream(endpoint.getOutputStream());
			primitiveOutputStream.writeInt(BlockFile.getHighestIndex());
			for(int i=1; i<=BlockFile.getHighestIndex(); i++) {
				objectOutputStream.writeObject(BlockFile.readBlock(i));
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
		this.endpoint=endpoint;
	}
}