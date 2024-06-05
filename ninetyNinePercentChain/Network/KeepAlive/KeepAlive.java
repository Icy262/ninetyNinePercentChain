package ninetyNinePercentChain.Network.KeepAlive;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.lang.Thread;
import java.net.SocketTimeoutException;

import ninetyNinePercentChain.Network.NodeIP;

class KeepAlive extends Thread {
	private String ip; //IP we are supposed to connect to.
	/*
	Name: run
	Description: Opens a connection with the ip passed in the constructor. Sets a timeout for 10s. If the connection is not accepted and data sent within 10s, an error is thrown. If the error is thrown, we remove the IP from the IP lsit.
	Precondition: None
	Postcondition: IP removed if connection not accepted. IP left if not.
	*/
	public void run() {
		try {
			int port=9939;
			Socket socket=new Socket(ip, port); //Creates a connection with the node we are trying to check
			socket.setSoTimeout(10000); //Blocking method call times out after 10 seconds if there is not a message sent. Throws an error when it times out
			ObjectInputStream read=new ObjectInputStream(socket.getInputStream()); //Open an ObjectInputStream to the socket. This will let us read from it or throw a timeout
			read.read(); //Blocks. Doesn't matter what data is read, as long as something is recieved within 10 seconds
			read.close(); //Close the InputStream to prevent wasted system resouces
			socket.close(); //Close the Socket to prevent wasted system resouces
		} catch(Exception e) {
			if(e instanceof SocketTimeoutException) {
				NodeIP.removeIP(ip); //Remove IP because it did not respond in time
			} else {
				System.out.println(e);
			}
		}
	}
	/*
	Name: KeepAlive
	Description: Initializes the value of the ip
	Precondition: ip is a valid ip address of a computer running ninetyNinePercentChain
	Postcondition: this.ip set to ip
	*/
	public KeepAlive(String ip) {
		this.ip=ip;
	}
}