package org.ninetyNinePercentChain;
import org.ninetyNinePercentChain.Network.DNS.QueryDNS;
import org.ninetyNinePercentChain.Network.DNS.RegisterDNS;
import org.ninetyNinePercentChain.Network.InterNode.Read.NetworkReceiveHandler;
import org.ninetyNinePercentChain.Network.InterNode.Sync.SyncChainResponseManager;
import org.ninetyNinePercentChain.Network.KeepAlive.KeepAliveManager;
import org.ninetyNinePercentChain.Network.KeepAlive.KeepAliveResponse;

public class Main {
	public static void main(String[] args) {
		new RegisterDNS().start();
		new QueryDNS().start();
		new KeepAliveResponse().start();
		new KeepAliveManager().start();
		new NetworkReceiveHandler(true).start();
		new SyncChainResponseManager().start();
	}
}