package ninetyNinePercentChain.Network.KeepAlive;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectOutputStream;
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
			while(continueRunning) { //While we should continue running,
				Socket socket=serverSocket.accept(); //Accepts an incoming connection
				ObjectOutputStream writer=new ObjectOutputStream(socket.getOutputStream()); //Makes an output stream. This lets us write data to the endpoint, which will prevent them from removing our IP
				writer.write(0); //Writes arbitrary data
				writer.close(); //Cleans up the writer so that we don't waste resouces
				socket.close(); //Closes connection.
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