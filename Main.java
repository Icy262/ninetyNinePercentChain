import ninetyNinePercentChain.KeepAliveManager;
import ninetyNinePercentChain.KeepAliveResponse;
import ninetyNinePercentChain.NetworkReceiveHandler;
import ninetyNinePercentChain.QueryDNS;
import ninetyNinePercentChain.RegisterDNS;
import ninetyNinePercentChain.SyncChainResponseManager;

public class Main {
	public static void main(String[] args) {
		new RegisterDNS().start();
		new QueryDNS().start();
		new KeepAliveResponse().start();
		new KeepAliveManager().start();
		new NetworkReceiveHandler().start();
		new SyncChainResponseManager().start();
	}
}