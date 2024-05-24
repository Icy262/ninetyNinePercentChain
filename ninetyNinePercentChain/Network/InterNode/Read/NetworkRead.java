package ninetyNinePercentChain.Network.InterNode.Read;

import ninetyNinePercentChain.Block.Block;
import ninetyNinePercentChain.Block.Transaction;
import ninetyNinePercentChain.Block.Hashing.CheckValidity;
import ninetyNinePercentChain.Block.Hashing.FindBlockHashManager;
import ninetyNinePercentChain.NetworkTransaction.WaitForTransactionManager;
import ninetyNinePercentChain.Utils.BlockFile;

public class NetworkRead {
	private static boolean hashing;
	public static void add(Object toAdd) {
		if(toAdd.getClass()==Transaction.class) {
			if(CheckValidity.checkTransaction((Transaction) toAdd)&&hashing==true) {
				FindBlockHashManager.addTransaction((Transaction) toAdd);
			} else {
				WaitForTransactionManager.update((Transaction) toAdd);
			}
		} else if(toAdd.getClass()==Block.class) {
			if(CheckValidity.checkBlock((Block) toAdd)) {
				BlockFile.writeBlock((Block) toAdd);
			}
		}
	}
	public static void setHashing(boolean hashing) {
		NetworkRead.hashing=hashing;
	}
}