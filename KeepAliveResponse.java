import java.net.ServerSocket;
import java.lang.Thread;

class KeepAliveResponse extends Thread {
	int port=9937;
	public void run() {
		ServerSocket serverSocket=new ServerSocket(port);
		while(true) {
			serverSocket.accept();
			serverSocket.close();
		}
	}
}
