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