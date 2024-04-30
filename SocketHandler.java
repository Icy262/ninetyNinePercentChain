import java.net.ServerSocket;
import java.net.*;
import java.io.*;

public class SocketHandler extends Thread {
	public void run() {
		try {
			connect();
		} catch(Exception e) {
		}
	}
	public Transaction connect() throws Exception {
		int port=9925;
		try {
			ServerSocket server=new ServerSocket(port);
			Socket socket=server.accept();
			ObjectInputStream test=new ObjectInputStream(socket.getInputStream());
			Transaction testTransaction=(Transaction)test.readObject();
			test.close();
			server.close();
			return testTransaction;
		} catch(Exception e) {
			System.out.println(e);
			throw new Exception();
		}
	}
}
