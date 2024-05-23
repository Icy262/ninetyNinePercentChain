package ninetyNinePercentChain.Network.InterNode.Sync;
import java.net.ServerSocket;

public class SyncChainResponseManager extends Thread {
	public void run() {
		try(ServerSocket endpoint=new ServerSocket(9942)) {
			while(true) {
				new SyncChainResponder(endpoint.accept()).start();
			}
		} catch(Exception e) {
			System.out.println(e);
		}
	}
}