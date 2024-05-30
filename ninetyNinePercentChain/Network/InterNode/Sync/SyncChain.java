package ninetyNinePercentChain.Network.InterNode.Sync;
import java.io.ObjectInputStream;
import java.net.Socket;

import ninetyNinePercentChain.Block.Block;
import ninetyNinePercentChain.Network.InterNode.Read.NetworkRead;

import java.io.DataInputStream;

public class SyncChain extends Thread {
	private String ip;
	/*
	Name: run
	Description: Opens a socket connection. Reads a number of blocks being transfered. Reads an object from the stream that many times. Passes all the objects on to the NetworkRead to be handled appropriately.
	Precondition: None
	Postcondition: All objects recieved are added to the NetworkRead queue
	*/
	public void run() {
		try(Socket endpoint=new Socket(ip, 9942)) {
				DataInputStream primitiveInputStream=new DataInputStream(endpoint.getInputStream());
				ObjectInputStream objectInputStream=new ObjectInputStream(endpoint.getInputStream());
				for(int i=0; i<primitiveInputStream.readInt(); i++) {
					NetworkRead.add((Block) objectInputStream.readObject());
				}
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	/*
	Name: SyncChain
	Description: Constructor. Allows us to specify the ip to connect to
	Precondition: ip is a valid ip address and there is a node running on that ip
	Postcondition: this.ip set to ip
	*/
	public SyncChain(String ip) {
		this.ip=ip;
	}
}