package ninetyNinePercentChain.NetworkTransaction;

import ninetyNinePercentChain.Network.DNS.QueryDNS;

class NetworkInterface {
	public static void setup() {
		new QueryDNS().start();
	}
}