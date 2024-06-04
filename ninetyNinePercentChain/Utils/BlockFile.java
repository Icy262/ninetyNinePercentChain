package ninetyNinePercentChain.Utils;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import ninetyNinePercentChain.Block.Block;
import ninetyNinePercentChain.Block.Transaction;
import ninetyNinePercentChain.Block.TransactionIn;
import ninetyNinePercentChain.Block.TransactionOut;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;

public class BlockFile {
	/*
	Name: readBlock
	Description: Reads block number blockNum from the file and returns it
	Precondition: Block number blockNum exists
	Postcondition: Block number blockNum returned
	*/
	public static Block readBlock(int blockNum) {
		try {
			FileInputStream blockFile=new FileInputStream("./blockchain/"+String.valueOf(blockNum)+".ser");
			ObjectInputStream blockReader=new ObjectInputStream(blockFile);
			Block block=(Block) blockReader.readObject();
			blockReader.close();
			blockFile.close();
			return block;
		} catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}
	/*
	Name: writeBlock
	Description: Writes the passed block to a file named its index+".ser"
	Precondition: Block is initialized and valid
	Postcondition: Block written to file
	*/
	public static void writeBlock(Block toWrite) {
		try {
			FileOutputStream blockFile=new FileOutputStream("./blockchain/"+String.valueOf(toWrite.getIndex())+".ser");
			ObjectOutputStream blockWriter=new ObjectOutputStream(blockFile);
			blockWriter.writeObject(toWrite);
			blockWriter.close();
			blockFile.close();
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	/*
	Name: blockExists
	Description: Checks if block number index is in our blockchain directory
	Precondition: None
	Postcondition: True returned if it exists, false if not.
	*/
	public static boolean blockExists(int index) {
		try {
			FileInputStream block=new FileInputStream("./blockchain/"+index+".ser");
			block.close();
			return true;
		} catch(FileNotFoundException e) {
			return false;
		} catch(IOException e) {
			System.out.println(e);
			return true;
		}
	}
	/*
	Name: getHighestIndex
	Description: Gets the number of files in the blockchain directory
	Precondition: Only blocks are in blockchain directory. No missing blocks (eg. block 0, 1, 2, and 4 are present but 3 is not)
	Postcondition: Highest block index returned
	*/
	public static int getHighestIndex() {
		File directory=new File("./blockchain");
		return directory.listFiles().length-1;
	}
	/*
	Name: getTransaction
	Description: Takes a block index and transaction index and returns the transaction
	Precondition: Block index blockIndex exists and contains Transaction index transactionIndex.
	Postcondition: Transaction returned
	*/
	public static Transaction getTransaction(int blockIndex, int transactionIndex) {
		return readBlock(blockIndex).getTransaction(transactionIndex);
	}
	/*
	Name: getTIN
	Description: Takes a block index, transaction index, and TIN index and returns the TIN
	Precondition: Block index blockIndex exists and contains Transaction index transactionIndex which contains TransactionIn index TINIndex
	Postcondition: TIN returned
	*/
	public static TransactionIn getTIN(int blockIndex, int transactionIndex, int TINIndex) {
		return getTransaction(blockIndex, transactionIndex).getTIN(TINIndex);
	}
	/*
	Name: getOUT
	Description: Takes a block index, transaction index, and TOUT index and returns the TOUT
	Precondition: Block index blockIndex exists and contains Transaction index transactionIndex which contains TransactionOut index TOUTIndex
	Postcondition: TOUT returned
	*/
	public static TransactionOut getTOUT(int blockIndex, int transactionIndex, int TOUTIndex) {
		return getTransaction(blockIndex, transactionIndex).getTOUT(TOUTIndex);
	}
}