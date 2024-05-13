public class TransactionOut implements MerkleTreeable {
	byte[] nextTransactionPublicKey;
	public byte[] hash() {
		return SHA256Hash.hash(nextTransactionPublicKey);
	}
}