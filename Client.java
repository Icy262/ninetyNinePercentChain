import java.net.*;
import java.io.*; 

public class Client extends Thread {
	Transaction toSend;
	public Client(Transaction transaction) {
		toSend=transaction;
	}
	public void run() {
		runClient("127.0.0.1", 9925);
	}
	public void runClient(String ip, int port) {
		try {
			Socket socket=new Socket(ip, port);
			ObjectOutputStream test=new ObjectOutputStream(socket.getOutputStream());
			test.writeObject(toSend);
			test.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}