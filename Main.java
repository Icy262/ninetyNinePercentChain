import ninetyNinePercentChain.Network.DNS.QueryDNS;
import ninetyNinePercentChain.Network.DNS.RegisterDNS;
import ninetyNinePercentChain.Network.InterNode.Read.NetworkReceiveHandler;
import ninetyNinePercentChain.Network.InterNode.Sync.SyncChainResponseManager;
import ninetyNinePercentChain.Network.KeepAlive.KeepAliveManager;
import ninetyNinePercentChain.Network.KeepAlive.KeepAliveResponse;

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