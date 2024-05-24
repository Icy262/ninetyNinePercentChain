package ninetyNinePercentChain.Block.Hashing;
import java.nio.ByteBuffer;

import ninetyNinePercentChain.Block.Block;
import ninetyNinePercentChain.Utils.SHA256Hash;

class FindBlockHash extends Thread {
	Block blockToHash; //The block we are searching for a nonce for
	int numZeros; //The number of zeros that we are loking for
	/*
	Name: run
	Description: Converts the block to a byte array, hashes the byte array, and checks if it has the required number of zeros. If it does, we notify the block hash manager, if not, we increment the nonce and try again.
	Precondition: None
	Postcondition: Sets the nonce of the block once a valid hash with the required number of leading zeros is found.
	*/
	public void run() {
		byte[] headerAsByteArray=blockToHash.headerAsByteArray();
		ByteBuffer nonce=ByteBuffer.wrap(headerAsByteArray, 12, 8);
		nonce.asLongBuffer();
		do {
			nonce.putLong(12, nonce.getLong(12)+1);
		} while(!Block.checkHashZeros(SHA256Hash.hash(headerAsByteArray), numZeros));
		blockToHash.setNonce(nonce.getLong(12));
		FindBlockHashManager.validBlockFound(blockToHash);
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
}