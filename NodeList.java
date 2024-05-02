import java.util.ArrayList;

class NodeList {
	private static ArrayList<String[]> String[] ip=new ArrayList<String>();
	public static void setIP(int i, String value) {
		ip.set(i, value);
	}
	public static String getIP(int i) {
		return ip.get(i);
	}
}
