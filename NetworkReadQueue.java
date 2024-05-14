import java.util.ArrayList;

public class NetworkReadQueue {
	static ArrayList<Object> queue=new ArrayList<Object>();
	public static void addToQueue(Object toAdd) {
		queue.add(toAdd);
	}
	public static Object readQueue() {
		return queue.remove(0);
	}
}