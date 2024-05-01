import java.net.ServerSocket;
import java.lang.Thread;

class KeepAliveResponseManager extends Thread {
	int port=9937;
	public void run() {
		ServerSocket serverSocket=new ServerSocket(port);
		while(true) {
			new KeepAliveResponseSender(serverSocket.accept()).start();
		}
	}
}
