package ninetyNinePercentChain.Network.KeepAlive;
import java.net.ServerSocket;
import java.lang.Thread;

public class KeepAliveResponse extends Thread {
	private int port=9939;
	private boolean continueRunning=true;
	/*
	Name: run
	Description: Listens for incoming socket connections. Accepts the connection and then closes it. This prevents the node on the other end from throwing a timeout error and removing our IP from their node list.
	Precondition: None
	Postcondition: None
	*/
	public void run() {
		try(ServerSocket serverSocket=new ServerSocket(port);) {
			while(continueRunning) {
				serverSocket.accept().close();
			}
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	/*
	Name: stopThread
	Description: Sets the continueRunning flag to false. This stops the thread
	Precondition: None
	Postcondition: Thread stopped
	*/
	public void stopThread() {
		continueRunning=false;
	}
}