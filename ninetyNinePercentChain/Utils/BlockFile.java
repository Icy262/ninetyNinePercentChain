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

/*
 * The BlockFile class allows us to easily read and write Blocks to the blockchain directory. It also allows for easy access to Transactions, TransactionIns, and TransactionOuts in the blocks in the blockchain directory. It contains a method to find the number of blocks in the blockchain directory as well as to find if any given block exists.
 */

public class BlockFile {
	/*
	Name: readBlock
	Description: Reads block number blockNum from the file and returns it
	Precondition: Block number blockNum exists
	Postcondition: Block number blockNum returned
	*/
	public static Block readBlock(int blockNum) {
		try {
			FileInputStream blockFile=new FileInputStream("./blockchain/"+String.valueOf(blockNum)+".ser"); //Opens a file input stream to the block with the index blockNum
			ObjectInputStream blockReader=new ObjectInputStream(blockFile); //Opens a object input stream to the file
			Block block=(Block) blockReader.readObject(); //Reads the block contained in the file
			blockReader.close(); //Closes the reader to prevent wasted system resouces
			blockFile.close(); //Closes the file to prevent wasted resouces and locked files
			return block; //Returns the requested block
		} catch(Exception e) {
			System.out.println(e);
		}
		return null; //Some error
	}
	/*
	Name: writeBlock
	Description: Writes the passed block to a file named its index+".ser"
	Precondition: Block is initialized and valid
	Postcondition: Block written to file
	*/
	public static void writeBlock(Block toWrite) {
		try {
			FileOutputStream blockFile=new FileOutputStream("./blockchain/"+String.valueOf(toWrite.getIndex())+".ser"); //Opens an output stream to the file
			ObjectOutputStream blockWriter=new ObjectOutputStream(blockFile); //Opens and object output stream to the file. This lets us write the block
			blockWriter.writeObject(toWrite); //Writes the block to the store
			blockWriter.close(); //Closes the stream to prevent wasted resouces and locked files.
			blockFile.close(); //Closes the file to prevent wasted resources and locked files.
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
			FileInputStream block=new FileInputStream("./blockchain/"+index+".ser"); //Tries to read block number index
			block.close(); //Closes file to prevent locked file and wasted resouces
			return true; //Files exists, return true
		} catch(FileNotFoundException e) {
			return false; //File does not exist
		} catch(IOException e) {
			System.out.println(e);
			return false; //Some other error
		}
	}
	/*
	Name: getHighestIndex
	Description: Gets the number of files in the blockchain directory
	Precondition: Only blocks are in blockchain directory. No missing blocks (eg. block 0, 1, 2, and 4 are present but 3 is not)
	Postcondition: Highest block index returned
	*/
	public static int getHighestIndex() {
		File directory=new File("./blockchain"); //Opens the directory as a file
		return directory.listFiles().length-1; //Gets the number of files and subtracts one. This is because the highest index is one less than the number of files
	}
	/*
	Name: getTransaction
	Description: Takes a block index and transaction index and returns the transaction
	Precondition: Block index blockIndex exists and contains Transaction index transactionIndex.
	Postcondition: Transaction returned
	*/
	public static Transaction getTransaction(int blockIndex, int transactionIndex) {
		return readBlock(blockIndex).getTransaction(transactionIndex); //Reads block number blockIndex and gets transaction number transactionIndex
	}
	/*
	Name: getTIN
	Description: Takes a block index, transaction index, and TIN index and returns the TIN
	Precondition: Block index blockIndex exists and contains Transaction index transactionIndex which contains TransactionIn index TINIndex
	Postcondition: TIN returned
	*/
	public static TransactionIn getTIN(int blockIndex, int transactionIndex, int TINIndex) {
		return getTransaction(blockIndex, transactionIndex).getTIN(TINIndex); //Gets the transaction using getTransaction and then gets the specific TIN
	}
	/*
	Name: getOUT
	Description: Takes a block index, transaction index, and TOUT index and returns the TOUT
	Precondition: Block index blockIndex exists and contains Transaction index transactionIndex which contains TransactionOut index TOUTIndex
	Postcondition: TOUT returned
	*/
	public static TransactionOut getTOUT(int blockIndex, int transactionIndex, int TOUTIndex) {
		return getTransaction(blockIndex, transactionIndex).getTOUT(TOUTIndex); //Gets the transaction using getTransaction and then gets the specific TOUT we need
	}
}