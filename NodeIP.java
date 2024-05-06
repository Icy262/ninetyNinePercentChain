import java.util.ArrayList;

public class NodeIP {
	private static ArrayList<String> nodeIPs=new ArrayList<String>();
	static int currentIndex=0;
	public static void addIP(String ip) {
		for(int i=0; i<nodeIPs.size(); i++) {
			if(nodeIPs.get(i).equalsIgnoreCase(ip)) {
				return;
			}
		}
		nodeIPs.add(ip);
	}
	public static void removeIP(String ip) {
		nodeIPs.remove(ip);
	}
	public static String getIP(int index) {
		return nodeIPs.get(index);
	}
	public static int getSize() {
		return nodeIPs.size();
	}
	public static String getNextIP() {
		currentIndex=currentIndex++%nodeIPs.size();
		return getIP(currentIndex);
	}
}