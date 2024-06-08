package ninetyNinePercentChain.Network.KeepAlive;
import java.net.Socket;
import java.lang.Thread;
import java.net.ConnectException;

import ninetyNinePercentChain.Network.NodeIP;

/*
 * KeepAlive creates a connection to the ip passed in the constructor. If the IP does not accept the connection, we know the node is offline. An error is thrown and we remove the IP from our lists.
 */

class KeepAlive extends Thread {
	private String ip; //IP we are supposed to connect to.
	/*
	Name: run
	Description: Opens a connection with the ip passed in the constructor. If the connection is not accepted, an error is thrown. If the error is thrown, we remove the IP from the IP lsit.
	Precondition: None
	Postcondition: IP removed if connection not accepted. IP left if not.
	*/
	public void run() {
		try {
			int port=9939;
			Socket socket=new Socket(ip, port); //Creates a connection with the node we are trying to check
			socket.close(); //Close the Socket to prevent wasted system resouces
		} catch(Exception e) {
			if(e instanceof ConnectException) { //ConnectException is thrown if the node does not accept the connection
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