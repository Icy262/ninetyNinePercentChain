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
			FindBlockHash finder=new FindBlockHash();
			testBlock=finder.run(testBlock, 8);
			System.out.println();
			for(int i=0; i<32; i++) {
				for(int ii=0; ii<8; ii++) {
					System.out.print(testBlock.thisBlockHash[i]&0x1);
					testBlock.thisBlockHash[i]=(byte)(testBlock.thisBlockHash[i]>>1);
				}
				System.out.println(" "+i);
			}
		} catch(Exception e) {
		}
	}
}