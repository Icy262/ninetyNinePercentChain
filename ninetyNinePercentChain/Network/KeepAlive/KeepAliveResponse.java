package ninetyNinePercentChain.Network.KeepAlive;
import java.net.ServerSocket;
import java.lang.Thread;

public class KeepAliveResponse extends Thread {
	private int port=9939;
	public void run() {
		try(ServerSocket serverSocket=new ServerSocket(port);) {
			while(true) {
				serverSocket.accept().close();
			}
		} catch(Exception e) {
			System.out.println(e);
		}
	}
}