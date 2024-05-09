public class TransactionOut implements MerkleTreeable {
	byte[] nextTransactionPublicKey;
	public byte[] hash() {
		return new byte[32];
	}
}