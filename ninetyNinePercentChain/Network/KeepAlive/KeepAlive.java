package ninetyNinePercentChain.Network.KeepAlive;
import java.net.Socket;
import java.lang.Thread;
import java.net.SocketTimeoutException;

import ninetyNinePercentChain.Network.NodeIP;

class KeepAlive extends Thread {
	private String ip;
	/*
	Name: run
	Description: Opens a connection with the ip passed in the constructor. Sets a timeout for 10s. If the connection is not accepted within 10s, an error is thrown. If the error is thrown, we remove the IP from the IP lsit.
	Precondition: None
	Postcondition: IP removed if connection not accepted. IP left if not.
	*/
	public void run() {
		try {
			int port=9939;
			Socket socket=new Socket(ip, port);
			socket.setSoTimeout(10000);
			socket.close();
		} catch(Exception e) {
			if(e instanceof SocketTimeoutException) {
				NodeIP.removeIP(ip);
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