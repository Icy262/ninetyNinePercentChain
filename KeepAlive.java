import java.net.Socket;
import java.lang.Thread;

class KeepAlive extends Thread {
	public void run(String ip) throws SocketTimeoutException {
		int port=9937;
		Socket socket=new Socket(ip, port);
		socket.setSoTimeout(10000);
		socket.close();
	}
}
