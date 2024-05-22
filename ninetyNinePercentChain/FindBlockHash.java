package ninetyNinePercentChain;
import java.nio.ByteBuffer;

class FindBlockHash extends Thread {
	Block blockToHash;
	int numZeros;
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
	public FindBlockHash(Block blockToHash, int numZeros) {
		this.blockToHash=blockToHash;
		this.numZeros=numZeros;
	}
}