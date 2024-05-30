package ninetyNinePercentChain.Network.InterNode.Sync;
import java.net.ServerSocket;

public class SyncChainResponseManager extends Thread {
	private boolean continueRunning=true;
	/*
	Name: run
	Description: Listens for incoming socket connections. Accepts the connection and creates a new thread to handle it.
	Precondition: None
	Postcondition: Any incoming socket connections are accepted and handled in a new thread
	*/
	public void run() {
		try(ServerSocket endpoint=new ServerSocket(9942)) {
			while(continueRunning) {
				new SyncChainResponder(endpoint.accept()).start();
			}
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	/*
	Name: stopThread
	Description: Sets continueRunning flag to false. This stops the handler.
	Precondition: None
	Postcondition: Thread stopped.
	*/
	public void stopThread() {
		continueRunning=false;
	}
}