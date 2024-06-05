package ninetyNinePercentChain.Block.Hashing;

import java.util.ArrayList;

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
		BlockFile.writeBlock(validBlock); //Write the block to the blockchain directory
		NetworkSendManager.addToQueue(validBlock); //Send the block out to the other nodes
		block=null; //Clear the block
		search=null; //Clear the search thread
	}
	/*
	Name: addTransaction
	Description: Adds a new transaction to the block and restarts the process of finding a valid block hash.
	Precondition: newTransaction is valid transaction
	Postcondition: newTransaction is added to the block and the search thread is restarted
	*/
	public static void addTransaction(Transaction newTransaction) {
		if(search!=null) { //If there is a search thread running,
			search.stopThread(); //Sets continue running flag to false. This stops the thread
		}
		if(block!=null) { //If there is a block already being processed,
			block.addTransaction(newTransaction); //Add the transaction
		} else { //If there is no block,
			block=new Block(BlockFile.getHighestIndex()+1, System.currentTimeMillis(), BlockFile.readBlock(BlockFile.getHighestIndex()-1).hash(), new ArrayList<Transaction>()); //Create new block. Index is one greater than previous, timestamp is now, previousblockhash is the hash of the previous block. The ArrayList of transactions is empty.
			block.addTransaction(newTransaction); //Adds the new transaction
		}
		search=new FindBlockHash(block, 16); //Restarts the search thread.
	}
}