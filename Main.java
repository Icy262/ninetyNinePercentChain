public class Main {
    public static void main(String[] args) {
        try {
            Transaction tTest=null; //new Transaction();
            SocketHandler test=new SocketHandler();
            test.start();
            Client testClient=new Client(tTest);
            testClient.start();
        } catch(Exception e) {
        }
    }
}