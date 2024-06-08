package ninetyNinePercentChain.Network.InterNode.Write;
import java.io.ObjectOutputStream;
import java.lang.Thread;
import java.net.Socket;
import java.util.ArrayList;

/*
 * Network send creates a connection to another node. When objects are added to the send queue using the addToQueue method, we wake the thread. The run method of the class will remove the object at index 0 in the send queue, sends it over the socket, and wait again.
 */

class NetworkSend extends Thread {
	private String ip; //IP that we are supposed to connect to
	private ArrayList<Object> sendQueue=new ArrayList<Object>(); //List of Objects we are supposed to send
	private boolean continueRunning=true; //Flag to control if we continue running
	/*
	Name: NetworkSend
	Description: Constructor. Allows us to set the ip that we open a socket to
	Precondition: ip is a valid IP adress to a computer running ninetyNinePercentChain
	Postcondition: this.ip set to ip
	*/
	public NetworkSend(String ip) {
		this.ip=ip;
	}
	/*
	Name: run
	Description: Opens a socket to the endpoint and sends any objects passed to this object over the network
	Precondition: None
	Postcondition: None
	*/
	public void run() {
		try(Socket endpoint=new Socket(ip, 9938)) { //Our connection to the reciever
			ObjectOutputStream endpointOutputStream=new ObjectOutputStream(endpoint.getOutputStream()); //ObjectOutputStream. Allows us to write the object we are writing.
			while(continueRunning) { //While we should continue running,
				endpointOutputStream.writeObject(sendQueue.remove(0)); //Writes the object we are supposed to write
				wait(); //Waits. We will get notified whenever a new object is added to the queue
			}
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	/*
	Name: getIP
	Description: Returns the ip that this connection is to
	Precondition: Object has been initalized
	Postcondition: Value of ip returned
	*/
	public String getIP() {
		return ip;
	}
	/*
	Name: addToQueue
	Description: Adds the object to the sendQueue and wakes the thread up again
	Precondition: None
	Postcondition: Object added to send queue and woken up
	*/
	public void addToQueue(Object toSend) {
		sendQueue.add(toSend); //Adds the object to the queue
		synchronized(this) { //Lets us notify the thread
			notify(); //Wakes the thread so that it will send the object
		}
	}
	/*
	Name: stopThread
	Description: Sets the continueRunning flag to false. This stops the thread
	Precondition: None
	Postcondition: Thread is stopped
	*/
	public void stopThread() {
		continueRunning=false; //Sets continue running flag to false
		synchronized(this) { //Lets us notify the thread
			notify(); //Breaks out of the wait
		}
	}
}