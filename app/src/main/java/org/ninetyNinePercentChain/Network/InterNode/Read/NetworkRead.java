package org.ninetyNinePercentChain.Network.InterNode.Read;

import org.ninetyNinePercentChain.Block.Block;
import org.ninetyNinePercentChain.Block.Transaction;
import org.ninetyNinePercentChain.Block.Hashing.CheckValidity;
import org.ninetyNinePercentChain.Block.Hashing.FindBlockHashManager;
import org.ninetyNinePercentChain.NetworkTransaction.WaitForTransactionManager;
import org.ninetyNinePercentChain.Utils.BlockFile;

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