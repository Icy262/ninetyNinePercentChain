public class TransactionIn implements MerkleTreeable {
	int previousOutBlock; //Block number of previous transaction
	int previousOutTransaction; //Transaction number of previous transaction
	int previousOutOutputNumber; //The output number of the previous trasaction
	public byte[] hash() {
		return new byte[32];
	}
}