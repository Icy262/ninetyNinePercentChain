package ninetyNinePercentChain.Block;
import java.util.ArrayList;

import ninetyNinePercentChain.Block.Hashing.MerkleTree;
import ninetyNinePercentChain.Block.Hashing.MerkleTreeable;
import ninetyNinePercentChain.Utils.SHA256Hash;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.Serializable;

public class Block implements Serializable, MerkleTreeable {
	private int index;
	private long timestamp;
	private long nonce=0; //index 12-20
	private byte[] previousHash=new byte[32];
	private byte[] merkleRoot; //Merkle root of the transactions
	private ArrayList<Transaction> transactions=new ArrayList<Transaction>(); //Body
	/*
	Name: headerAsByteArray
	Description: Converts the header to a byte array. This is so that the header can be hashed eg. for merkle trees.
	Precondition: All values have been initialized
	Postcondition: Header converted to byte array in a deterministic manner
	*/
	public byte[] headerAsByteArray() {
		try {
			ByteArrayOutputStream headerAsByteArray=new ByteArrayOutputStream();
			DataOutputStream longWriter=new DataOutputStream(headerAsByteArray);
			headerAsByteArray.write(index);
			longWriter.writeLong(timestamp);
			longWriter.writeLong(nonce);
			longWriter.flush();
			headerAsByteArray.write(previousHash);
			headerAsByteArray.write(merkleRoot);
			return headerAsByteArray.toByteArray();
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
		return SHA256Hash.hash(headerAsByteArray());
	}
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
		merkleRoot=new MerkleTree<Transaction>(transactions).genTree();
	}
	/*
	Name: Block
	Description: Allows the block to be created and values to be set
	Precondition: Index is not negative, zero, and is one greater than the index of the previous block. Timestamp is the time the block was created. It should be the number of milliseconds since midnight of Jan 1, 1970. Transactions should include the transactions in the block and should not be null.
	Postcondition: All values initialized and the merkle root generated
	*/
	public Block(int index, long timestamp, byte[] previousHash, ArrayList<Transaction> transactions) {
		this.index=index;
		this.timestamp=timestamp;
		this.previousHash=previousHash;
		this.transactions=transactions;
		genMerkleRoot();
	}
	/*
	Name: addTransaction
	Description: Adds a transaction to the block and regenerates the merkle root
	Precondition: toAdd is a valid transaction
	Postcondition: toAdd added to the list of transactions and merkle root updated
	*/
	public void addTransaction(Transaction toAdd) {
		transactions.add(toAdd);
		genMerkleRoot();
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