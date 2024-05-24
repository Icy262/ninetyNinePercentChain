package ninetyNinePercentChain.Block.Hashing;

import ninetyNinePercentChain.Block.Block;
import ninetyNinePercentChain.Block.Transaction;
import ninetyNinePercentChain.Network.InterNode.Write.NetworkSendManager;
import ninetyNinePercentChain.Utils.BlockFile;

public class FindBlockHashManager {
	private static FindBlockHash search; //This is thread that is searching for the nonce that produces a valid block
	private static Block block; //The block we are currently trying to search for
	/*
	Name: validBlockFound
	Description: Handles a found valid block.
	Precondition: validBlock is a valid block
	Postcondition: Valid block written to the blockchain directory, and added to the network queue
	*/
	public static void validBlockFound(Block validBlock) {
		BlockFile.writeBlock(validBlock);
		NetworkSendManager.addToQueue(validBlock);
		block=null;
		search=null;
	}
	/*
	Name: addTransaction
	Description: Adds a new transaction to the block and restarts the process of finding a valid block hash.
	Precondition: newTransaction is valid transaction
	Postcondition: newTransaction is added to the block and the search thread is restarted
	*/
	public static void addTransaction(Transaction newTransaction) {
		if(search!=null) {
			search.interrupt();
		}
		block.addTransaction(newTransaction);
		search=new FindBlockHash(block, 16);
	}
}