import java.nio.ByteBuffer;

public class FindBlockHash /*extends Thread*/ {
	public Block run(Block blockToHash, int numZeros) {
		byte[] blockAsByteArray=blockToHash.blockAsByteArray();
		ByteBuffer nonce=ByteBuffer.wrap(blockAsByteArray, 12, 8);
		nonce.asLongBuffer();
		do {
			nonce.putLong(12, nonce.getLong(12)+1);
		} while(!Block.checkHashZeros(Block.hashBlock(blockAsByteArray), numZeros));
		blockToHash.nonce=nonce.getLong(12);
		blockToHash.thisBlockHash=Block.hashBlock(blockAsByteArray);
		return blockToHash;
	}
}