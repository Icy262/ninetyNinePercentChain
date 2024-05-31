package ninetyNinePercentChain.Network.InterNode.Write;
import java.io.ObjectOutputStream;
import java.lang.Thread;
import java.net.Socket;
import java.util.ArrayList;

class NetworkSend extends Thread {
	private String ip;
	private ArrayList<Object> sendQueue=new ArrayList<Object>();
	private boolean continueRunning=true;
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
		try(Socket endpoint=new Socket(ip, 9938)) {
			ObjectOutputStream endpointOutputStream=new ObjectOutputStream(endpoint.getOutputStream());
			while(continueRunning) {
				endpointOutputStream.writeObject(sendQueue.remove(0));
				wait();
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
		sendQueue.add(toSend);
		notify();
	}
	/*
	Name: stopThread
	Description: Sets the continueRunning flag to false. This stops the thread
	Precondition: None
	Postcondition: Thread is stopped
	*/
	public void stopThread() {
		continueRunning=false;
	}
}