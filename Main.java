import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        try {
            Transaction tTest=null; //new Transaction();
            SocketHandler test=new SocketHandler();
            test.start();
            Client testClient=new Client(tTest);
            testClient.start();
            Block testBlock=new Block(0, System.currentTimeMillis(), new byte[32], new ArrayList<Transaction>());
            byte[] testBlockByteArray=testBlock.blockAsByteArray();
            testBlockByteArray=Block.hashBlock(testBlockByteArray);
            System.out.println();
            for(int i=0; i<testBlockByteArray.length; i++) {
                System.out.print(testBlockByteArray[i]+" ");
            }
        } catch(Exception e) {
        }
    }
}