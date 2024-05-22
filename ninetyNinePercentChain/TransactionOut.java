package ninetyNinePercentChain;
class TransactionOut implements MerkleTreeable {
	private byte[] nextTransactionPublicKey; //Public key that corresponds to the private key that the next transaction must be signed with
	private int value; //Value of transaction
	public byte[] hash() {
		return SHA256Hash.hash(nextTransactionPublicKey);
	}
	public int getValue() {
		return value;
	}
	public byte[] getNextTransactionPublicKey() {
		return nextTransactionPublicKey;
	}
	public TransactionOut(byte[] nextTransactionPublicKey, int value) {
		this.nextTransactionPublicKey=nextTransactionPublicKey;
		this.value=value;
	}
}