import java.net.ServerSocket;
import java.lang.Thread;

class KeepAliveResponse extends Thread {
	int port=9937;
	public void run() {
		try {
			ServerSocket serverSocket=new ServerSocket(port);
			while(true) {
				serverSocket.accept();
				serverSocket.close();
			}
		} catch(Exception e) {
			System.out.println(e);
		}
	}
}
