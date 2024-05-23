package ninetyNinePercentChain.Network.InterNode.Sync;
import java.io.DataOutputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import ninetyNinePercentChain.Utils.BlockFile;

class SyncChainResponder extends Thread {
	private Socket endpoint;
	public void run() {
		try {
			DataOutputStream primitiveOutputStream=new DataOutputStream(endpoint.getOutputStream());
			ObjectOutputStream objectOutputStream=new ObjectOutputStream(endpoint.getOutputStream());
			primitiveOutputStream.writeInt(BlockFile.getHighestIndex());
			for(int i=1; i<=BlockFile.getHighestIndex(); i++) {
				objectOutputStream.writeObject(BlockFile.readBlock(i));
			}
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	public SyncChainResponder(Socket endpoint) {
		this.endpoint=endpoint;
	}
}