package ninetyNinePercentChain.Network.InterNode.Sync;
import java.net.ServerSocket;

/*
 * Listens for incoming requests from other nodes. Creates a new responder thread and passes the connection to it.
 */

public class SyncChainResponseManager extends Thread {
	private boolean continueRunning=true; //Controls if the thread should continue running
	/*
	Name: run
	Description: Listens for incoming socket connections. Accepts the connection and creates a new thread to handle it.
	Precondition: None
	Postcondition: Any incoming socket connections are accepted and handled in a new thread
	*/
	public void run() {
		try(ServerSocket endpoint=new ServerSocket(9942)) { //Listens for connections,
			while(continueRunning) { //While the thread should continue running,
				new SyncChainResponder(endpoint.accept()).start(); //Accepts the connection and starts a new thread to handle it
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
		continueRunning=false; //Sets continue running flag to false
	}
}