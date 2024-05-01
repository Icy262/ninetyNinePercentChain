import java.net.Socket;
import java.lang.Thread;
import java.io.ObjectInputStream;

class QueryDNS extends Thread {
	public void run() {
		int port=9937;
		String DNSIP="ninetyNinePercent.mrman314.tech";
		String[] ip=new String[10];
		try {
			Socket socket=new Socket(DNSIP, port);
			ObjectInputStream read=new ObjectInputStream(socket.getInputStream());
			for(int i=0; i<10; i++) {
				ip[i]=(String) read.readObject();
			}
			read.close();
			socket.close();
			//ADD ip TO LIST
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
