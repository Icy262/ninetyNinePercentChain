package ninetyNinePercentChain.Network.InterNode.Sync;
import java.io.ObjectInputStream;
import java.net.Socket;

import ninetyNinePercentChain.Block.Block;
import ninetyNinePercentChain.Network.InterNode.Read.NetworkRead;

import java.io.DataInputStream;

public class SyncChain extends Thread {
	private String ip;
	public void run() {
		try(Socket endpoint=new Socket(ip, 9942)) {
				DataInputStream primitiveInputStream=new DataInputStream(endpoint.getInputStream());
				ObjectInputStream objectInputStream=new ObjectInputStream(endpoint.getInputStream());
				for(int i=0; i<primitiveInputStream.readInt(); i++) {
					NetworkRead.add((Block) objectInputStream.readObject());
				}
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	public SyncChain(String ip) {
		this.ip=ip;
	}
}