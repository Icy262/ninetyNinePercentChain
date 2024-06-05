package ninetyNinePercentChain.Network.DNS;
import java.net.Socket;
import java.lang.Thread;
import java.io.ObjectOutputStream;

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
			write.writeObject(socket.getInetAddress().toString()); //Writes our IP address in String format to the DNS server
			write.close(); //Closes the ObjectOutputStream to save resources
			socket.close(); //Close the Socket to save resources
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}