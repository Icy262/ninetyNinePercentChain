package ninetyNinePercentChain.Network.DNS;
import java.net.Socket;

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
		String DNSIP="127.98.108.54";
		try {
			Socket socket=new Socket(DNSIP, port); //Creates a connection to the DNS server
			ObjectOutputStream write=new ObjectOutputStream(socket.getOutputStream()); //Opens a ObjectOutputStream
			write.writeUTF(socket.getLocalAddress().getHostAddress()); //Writes our IP address in String format to the DNS server
			write.close(); //Closes the ObjectOutputStream to save resources
			socket.close(); //Close the Socket to save resources
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}