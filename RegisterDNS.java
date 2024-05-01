import java.net.ServerSocket;
import java.lang.Thread;
import java.io.ObjectOutputStream;

class RegisterDNS extends Thread {
	public void run() {
		int port=9937;
		String DNSIP="ninetyNinePercent.mrman314.tech";
		try {
			Socket socket=new Socket(ip, port);
			ObjectOutputStream write=new ObjectOutputStream(socket.getOutputStream());
			write.writeObject(socket.getInetAddress().toString());
			write.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
