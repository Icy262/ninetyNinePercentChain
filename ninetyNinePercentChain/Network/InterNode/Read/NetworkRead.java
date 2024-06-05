package ninetyNinePercentChain.Network.InterNode.Read;

import ninetyNinePercentChain.Block.Block;
import ninetyNinePercentChain.Block.Transaction;
import ninetyNinePercentChain.Block.Hashing.CheckValidity;
import ninetyNinePercentChain.Block.Hashing.FindBlockHashManager;
import ninetyNinePercentChain.NetworkTransaction.WaitForTransactionManager;
import ninetyNinePercentChain.Utils.BlockFile;

public class NetworkRead {
	private static boolean hashing;
	/*
	Name: add
	Description: Takes an object. If it is a Transaction object, we check the validity and if it is valid, we either give it to the FindBlockHashManager if the hashing flag is true or we ignore it. If it is a Block, we check if it is valid, and if it is valid, we write to our chain directory. If we have hashing set to false, we also notify the WaitForTransactionManager.
	Precondition: None
	Postcondition: Input object handled appropriately
	*/
	public static void add(Object toAdd) {
		if(toAdd.getClass()==Transaction.class) { //If the object is a transaction
			if(CheckValidity.checkTransaction((Transaction) toAdd)&&hashing) { //If we have hashing on and the Transaction is valid,
				FindBlockHashManager.addTransaction((Transaction) toAdd);  //Pass the transaction to the FindBlockHashManager
			} //If hashing is off, we just ignore the transaction
		} else if(toAdd.getClass()==Block.class) { //If the object is a Block,
			if(CheckValidity.checkBlock((Block) toAdd)) { //If the block is valid,
				BlockFile.writeBlock((Block) toAdd); //Write the block to our local copy of the blockchain
				if(!hashing) { //If we don't have hashing on,
					WaitForTransactionManager.update((Block) toAdd); //Update the WaitForTransactionManager of a new block
				}
			}
		}
	}
	/*
	Name: setHashing
	Description: Allows us to set the value of the hashing flag. If hashing is true, we try to hash incoming transactions, if not, we ignore it. If hashing is false, the node is in listening mode and should tell the WaitForTransactionManager about any new blocks.
	Precondition: None
	Postcondition: The value of the hashing flag set to the passed value
	*/
	public static void setHashing(boolean hashing) {
		NetworkRead.hashing=hashing;
	}
}