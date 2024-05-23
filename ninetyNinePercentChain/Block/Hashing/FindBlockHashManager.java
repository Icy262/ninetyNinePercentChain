package ninetyNinePercentChain.Block.Hashing;

import ninetyNinePercentChain.Block.Block;
import ninetyNinePercentChain.Block.Transaction;
import ninetyNinePercentChain.Network.InterNode.Write.NetworkSendManager;
import ninetyNinePercentChain.Utils.BlockFile;

public class FindBlockHashManager {
	private static FindBlockHash search;
	private static Block block;
	public static void validBlockFound(Block validBlock) {
		BlockFile.writeBlock(validBlock);
		NetworkSendManager.addToQueue(validBlock);
		block=null;
		search=null;
	}
	public static void addTransaction(Transaction newTransaction) {
		if(search!=null) {
			search.interrupt();
		}
		block.addTransaction(newTransaction);
		search=new FindBlockHash(block, 16);
	}
}