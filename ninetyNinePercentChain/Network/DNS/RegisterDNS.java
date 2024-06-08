package ninetyNinePercentChain.Network.DNS;
import java.net.Socket;

import ninetyNinePercentChain.Network.NodeIP;

import java.lang.Thread;
import java.io.ObjectOutputStream;

/*
 * Connects to the DNS server and sends our IP to it. The DNS server will store this and will serve it to other Nodes calling QueryDNS
 */

public class RegisterDNS extends Thread {
	/*
	Name: run
	Description: Opens a socket to the DNS Seed server. Sends the public IP address of this computer.
	Precondition: None
	Postcondition: None
	*/
	public void run() {
		int port=9941;
		String DNSIP="10.10.166.222";
		try {
			Socket socket=new Socket(DNSIP, port); //Creates a connection to the DNS server
			ObjectOutputStream write=new ObjectOutputStream(socket.getOutputStream()); //Opens a ObjectOutputStream
			write.writeObject(socket.getLocalAddress().getHostAddress()); //Writes our IP address in String format to the DNS server
			NodeIP.setLocalIP(socket.getLocalAddress().getHostAddress()); //Passes our IP address to the NodeIP
			write.close(); //Closes the ObjectOutputStream to save resources
			socket.close(); //Close the Socket to save resources
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}