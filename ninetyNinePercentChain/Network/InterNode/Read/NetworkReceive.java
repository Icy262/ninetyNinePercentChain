package ninetyNinePercentChain.Network.InterNode.Read;
import java.net.Socket;
import java.io.ObjectInputStream;

class NetworkReceive extends Thread {
	private Socket endpoint;
	public void run() {
		try {
			ObjectInputStream endpointInputStream=new ObjectInputStream(endpoint.getInputStream());
			while(true) {
				NetworkRead.add(endpointInputStream.readObject());
			}
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	public NetworkReceive(Socket endpoint) {
		this.endpoint=endpoint;
	}
}