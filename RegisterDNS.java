import java.net.Socket;
import java.lang.Thread;
import java.io.ObjectOutputStream;

class RegisterDNS extends Thread {
	public void run() {
		int port=9941;
		String DNSIP="ninetyNinePercent.mrman314.tech";
		try {
			Socket socket=new Socket(DNSIP, port);
			ObjectOutputStream write=new ObjectOutputStream(socket.getOutputStream());
			write.writeObject(socket.getInetAddress().toString());
			write.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}