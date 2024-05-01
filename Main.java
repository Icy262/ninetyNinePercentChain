public class Main {
	public static void main(String[] args) {
		try {
			KeepAlive test=new KeepAlive("127.0.0.1");
			test.run();
		} catch(Exception e) {
			System.out.println("\nMAIN "+e);
		}
	}
}