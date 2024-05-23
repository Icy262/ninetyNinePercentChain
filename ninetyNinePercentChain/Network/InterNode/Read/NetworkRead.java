package ninetyNinePercentChain.Network.InterNode.Read;

import java.util.ArrayList;

import ninetyNinePercentChain.Block.Block;
import ninetyNinePercentChain.Block.Transaction;
import ninetyNinePercentChain.Block.Hashing.CheckValidity;
import ninetyNinePercentChain.Block.Hashing.FindBlockHashManager;
import ninetyNinePercentChain.Utils.BlockFile;

public class NetworkRead {
	private static boolean hashing;
	private static ArrayList<Transaction> queue;
	public static void add(Object toAdd) {
		if(toAdd.getClass()==Transaction.class) {
			if(CheckValidity.checkTransaction((Transaction) toAdd)&&hashing==true) {
				FindBlockHashManager.addTransaction((Transaction) toAdd);
			} else {
				queue.add((Transaction)toAdd);
			}
		} else if(toAdd.getClass()==Block.class) {
			if(CheckValidity.checkBlock((Block) toAdd)) {
				BlockFile.writeBlock((Block) toAdd);
			}
		}
	}
	public static Transaction getQueue() {
		return queue.remove(0);
	}
	public static void setHashing(boolean hashing) {
		NetworkRead.hashing=hashing;
	}
}