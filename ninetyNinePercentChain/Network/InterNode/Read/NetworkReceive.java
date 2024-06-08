package ninetyNinePercentChain.Network.InterNode.Read;
import java.net.Socket;
import java.io.ObjectInputStream;

/*
 * Constructor passes a socket connection to the thread. Thread continously reads from the socket and passes the data to NetworkRead
 */

class NetworkReceive extends Thread {
	private Socket endpoint;
	private boolean continueRunning=true;
	/*
	Name: run
	Description: Creates a ObjectInputStream with the other node and reads objects from it
	Precondition: Has a valid Socket connection with the endpoint
	Postcondition: Any recieved objects are passed to the NetworkRead
	*/
	public void run() {
		try {
			ObjectInputStream endpointInputStream=new ObjectInputStream(endpoint.getInputStream()); //Creates a new ObjectInputStream with the node
			while(continueRunning) { //While the thread should continue running,
				NetworkRead.add(endpointInputStream.readObject()); //Read any object coming over the network and pass it to the handler.
			}
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	/*
	Name: NetworkRecieve
	Description: Constructor. Sets our Socket to the Socket that the NetworkRecieveHander created
	Precondition: Socket is a valid socket to the endpoint
	Postcondition: Socket endpoint is initalized
	*/
	public NetworkReceive(Socket endpoint) {
		this.endpoint=endpoint;
	}
	/*
	Name: stopThread
	Description: Sets the continueRunning flag to false
	Precondition: None
	Postcondition: continueRunning set to false.
	*/
	public void stopThread() {
		continueRunning=false;
	}
}