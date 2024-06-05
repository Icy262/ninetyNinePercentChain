package ninetyNinePercentChain.Network.InterNode.Sync;
import java.io.ObjectInputStream;
import java.net.Socket;

import ninetyNinePercentChain.Block.Block;
import ninetyNinePercentChain.Network.InterNode.Read.NetworkRead;

import java.io.DataInputStream;

public class SyncChain extends Thread {
	private String ip; //The IP we should open a connection to
	/*
	Name: run
	Description: Opens a socket connection. Reads a number of blocks being transfered. Reads an object from the stream that many times. Passes all the objects on to the NetworkRead to be handled appropriately.
	Precondition: None
	Postcondition: All objects recieved are added to the NetworkRead queue
	*/
	public void run() {
		try(Socket endpoint=new Socket(ip, 9942)) { //Opens a connection to the ip
				DataInputStream primitiveInputStream=new DataInputStream(endpoint.getInputStream()); //DataInputStream to the Socket. Allows us to read primitive data
				ObjectInputStream objectInputStream=new ObjectInputStream(endpoint.getInputStream()); //ObjectInputStream to the Socket. Allows us to read objects, such as blocks
				for(int i=0; i<primitiveInputStream.readInt(); i++) { //Gets the number of blocks being transferred. Runs one for each block to be transferred,
					NetworkRead.add((Block) objectInputStream.readObject()); //Reads the object, casts it to a block, and passes it to NetworkRead to be handled appropriately
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