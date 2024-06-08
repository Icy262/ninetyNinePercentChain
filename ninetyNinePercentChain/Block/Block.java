package ninetyNinePercentChain.Block;
import java.util.ArrayList;

import ninetyNinePercentChain.Block.Hashing.MerkleTree;
import ninetyNinePercentChain.Block.Hashing.MerkleTreeable;
import ninetyNinePercentChain.Utils.SHA256Hash;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.Serializable;

/*
 * The Block class holds a collection of transactions. Blocks are "chained" together to form a blockchain. Each block's index is one greater than the one before it. Each block has a copy of the previous block's hash in the header. This means that if any change is made to a block, every single block that comes after it would need to be adjusted. Block uses a merkle root of the transaction's hashes in order to prevent any of the transactions from being changed
 */

public class Block implements Serializable, MerkleTreeable {
	private int index; //Header. Index number of the block. This lets us find specific blocks, and to tell the order of blocks and transactions
	private long timestamp; //Header. Time the block was created at
	private long nonce=0; //Allows us to change the hash without changing the data stored inside. This allows us to find hashes with specific numbers of leading zeros. Takes up index 12-20 in headerAsByteArray
	private byte[] previousHash; //The previous hash. This prevents someone from going back and changing one of the old blocks
	private byte[] merkleRoot; //Merkle root of the transactions
	private ArrayList<Transaction> transactions=new ArrayList<Transaction>(); //Body. Stores all the transactions in the block.
	/*
	Name: headerAsByteArray
	Description: Converts the header to a byte array. This is so that the header can be hashed eg. for merkle trees.
	Precondition: All values have been initialized
	Postcondition: Header converted to byte array in a deterministic manner
	*/
	public byte[] headerAsByteArray() {
		try {
			ByteArrayOutputStream headerAsByteArray=new ByteArrayOutputStream(); //Creates a ByteArrayOutputStream. This makes it easier to combine ints, longs, and byte[] all into one array
			DataOutputStream longWriter=new DataOutputStream(headerAsByteArray); //Allows us to easily convert a long to a byte[]. ByteArrayOutputStream does not have a method for writing longs.
			headerAsByteArray.write(index);
			longWriter.writeLong(timestamp);
			longWriter.writeLong(nonce);
			longWriter.close(); //Flushes all data out of the output stream and then closes it. This ensures there are no memory leaks or lost data. 
			headerAsByteArray.write(previousHash);
			headerAsByteArray.write(merkleRoot);
			return headerAsByteArray.toByteArray(); //Puts all the data in the ByteArrayOutputStream into a byte array and returns it
		} catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}
	/*
	Name: hashes
	Description: Converts the header to a byte array, hashes it, and returns it
	Precondition: All header values initialized
	Postcondition: Header hashed with SHA256 and returned. Same header values will always produce the same output.
	*/
	public byte[] hash() {
		return SHA256Hash.hash(headerAsByteArray()); //Converts header to byte array and hashes it.
	}
	//THIS USED TO BE IMPORTANT, BUT IT IS NO LONGER NEEDED BECAUSE WE DON'T REQUIRE ANY LEADING ZEROS ANYMORE. THIS WAS DISABLED TO IMPROVE TRANSACTION PROCESSING SPEED
	// /*
	// Name: checkHashZeros
	// Description: Checks if the number of zeros in the hash meets the standard set by the config
	// Precondition: numZeros is positive
	// Postcondition: True returned if there are at least numZero zeros in the beginning of hash. False returned if not.
	// */
	// public static boolean checkHashZeros(byte[] hash, int numZeros) {
	// 	for(int i=0; i<numZeros; i++) {
	// 		if(((hash[(int)(i/8)]>>(int)(i%8))&1)==1) {
	// 			return false; //Doesn't have numZeros
	// 		}
	// 	}
	// 	return true; //Has numZeros
	// }
	/*
	Name: genMerkleRoot
	Description: Takes the transactions contained in this block and generates the merkle root
	Precondition: The block has transactions
	Postcondition: The merkle root field in the header is set
	*/
	public void genMerkleRoot() {
		merkleRoot=new MerkleTree<Transaction>(transactions).genTree(); //Creates a new merkle tree object and passes the transactions to process. Generates the tree and returns it.
	}
	/*
	Name: Block
	Description: Allows the block to be created and values to be set
	Precondition: Index is not negative, zero, and is one greater than the index of the previous block. Timestamp is the time the block was created. It should be the number of milliseconds since midnight of Jan 1, 1970. Transactions should include the transactions in the block and should not be null.
	Postcondition: All values initialized and the merkle root generated
	*/
	public Block(int index, long timestamp, byte[] previousHash, ArrayList<Transaction> transactions) {
		this.index=index; //Block index. Should be one greater than the previous block
		this.timestamp=timestamp; //Time the block was created
		this.previousHash=previousHash; //Previous hash of the block. This prevents anyone from changing the previous block later.
		this.transactions=transactions; //All the transactions contained in this block. Can be empty to start as long as the object has been created.
		genMerkleRoot(); //Generates the merkle root. To ensure that it is initalized.
	}
	/*
	Name: addTransaction
	Description: Adds a transaction to the block and regenerates the merkle root
	Precondition: toAdd is a valid transaction
	Postcondition: toAdd added to the list of transactions and merkle root updated
	*/
	public void addTransaction(Transaction toAdd) {
		transactions.add(toAdd); //Adds the transaction to the list
		genMerkleRoot(); //Regens the Merkle Root. This changes with any change in the Transaction list, so we need to update it
	}
	/*
	Name: getIndex
	Description: Gets the value of the index
	Precondition: index has been initialized
	Postcondition: index is returned
	*/
	public int getIndex() {
		return index;
	}
	/*
	Name: getMerkleRoot
	Description: Returns the value of the merkle root
	Precondition: Merkle root has been generated
	Postcondition: Merkle root returned
	*/
	public byte[] getMerkleRoot() {
		return getMerkleRoot();
	}
	/*
	Name: getTransaction
	Description: Gets the Transaction at the index index in the transactions ArrayList and returns it
	Precondition: There exists a Transaction at index index
	Postcondition: Transaction at index index returned
	*/
	public Transaction getTransaction(int index) {
		return transactions.get(index);
	}
	/*
	Name: getNumTransactions
	Description: Gets the size of the transactions list and returns it
	Precondition: transactions has been initalized
	Postcondition: The size of the transactions ArrayList is returned
	*/
	public int getNumTransactions() {
		return transactions.size();
	}
	/*
	Name: getPreviousHash
	Description: Gets the value of the previousHash field
	Precondition: previousHash has been initialized
	Postcondition: Value of previousHash returned
	*/
	public byte[] getPreviousHash() {
		return previousHash;
	}
	/*
	Name: getAllTransactions
	Description: Returns the entire ArrayList of transactions
	Precondition: transactions has been initalized
	Postcondition: transactions is returned
	*/
	public ArrayList<Transaction> getAllTransactions() {
		return transactions;
	}
	/*
	Name: getNonce
	Description: Gets the value of the nonce
	Precondition: Nonce has been initialized
	Postcondition: Value of nonce returned
	*/
	public long getNonce() {
		return nonce;
	}
	/*
	Name: setNonce
	Description: Sets the value of the nonce
	Precondition: None
	Postcondition: Value of this.nonce set to nonce
	*/
	public void setNonce(long nonce) {
		this.nonce = nonce;
	}
}