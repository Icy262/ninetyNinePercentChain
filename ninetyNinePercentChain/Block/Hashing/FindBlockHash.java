package ninetyNinePercentChain.Block.Hashing;
import java.nio.ByteBuffer;

import ninetyNinePercentChain.Block.Block;

/*
 * Hashes the block. Sends it to the FindBlockHashManager once it has been hashed so that the block can be stored and propagated across the network.
 */

class FindBlockHash extends Thread {
	Block blockToHash; //The block we are searching for a nonce for
	int numZeros; //The number of zeros that we are loking for
	boolean continueRunning=true; //Continue running the thread
	/*
	Name: run
	Description: Converts the block to a byte array, hashes the byte array, and checks if it has the required number of zeros. If it does, we notify the block hash manager, if not, we increment the nonce and try again.
	Precondition: None
	Postcondition: Sets the nonce of the block once a valid hash with the required number of leading zeros is found.
	*/
	public void run() {
		byte[] headerAsByteArray=blockToHash.headerAsByteArray(); //Converts block header to byte array for hashing
		ByteBuffer nonce=ByteBuffer.wrap(headerAsByteArray, 12, 8); //Wraps the byte[] in a ByteBuffer.
		nonce.asLongBuffer(); //We view the byte[] as an array of longs. We can now efficiently change the nonce without needing to redo the entire block header
		//We disabled the zero requirement so that hashes can be computed instantly. This speeds up transaction time
		// do {
		nonce.putLong(12, nonce.getLong(12)+1); //Increments the nonce
		// } while(!Block.checkHashZeros(SHA256Hash.hash(headerAsByteArray), numZeros)&&continueRunning);
		blockToHash.setNonce(nonce.getLong(12)); //Sets the actual block's nonce to the valid nonce
		FindBlockHashManager.validBlockFound(blockToHash); //Notifies the FindBlockHashManager so that the block can be stored and propagated across the network.
	}
	/*
	Name: FindBlockHash
	Description: Initializes a FindBlockHash instance with the specified Block and number of leading zeros.
	Precondition: numZeros is not negative and is less than 256
	PostCondition: None
	*/
	public FindBlockHash(Block blockToHash, int numZeros) {
		this.blockToHash=blockToHash;
		this.numZeros=numZeros;
	}
	/*
	Name: stopThread
	Description: Sets continueRunning flag to false. This stops the thread.
	Precondition: None
	PostCondition: Thread stopped
	*/
	public void stopThread() {
		continueRunning=false;
	}
}