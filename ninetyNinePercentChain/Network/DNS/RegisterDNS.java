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
			Socket socket=new Socket(DNSIP, port);
			ObjectOutputStream write=new ObjectOutputStream(socket.getOutputStream());
			write.writeObject(socket.getInetAddress().toString());
			write.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}