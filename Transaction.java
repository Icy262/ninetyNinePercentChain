import java.io.Serializable;

public class Transaction implements Serializable, MerkleTreeable {
	TransactionIn[] TIN;
	TransactionOut[] TOUT;
	long timestamp=System.currentTimeMillis();

	public Transaction(TransactionIn[] TIN, TransactionOut[] TOUT, long timestamp) {
		this.TIN=TIN;
		this.TOUT=TOUT;
		this.timestamp=timestamp;
	}
	public byte[] hash() {
		return SHA256Hash.hash(blockAsByteArray());
	}
	public byte[] blockAsByteArray() {
		//IMPLEMENT PROPERLY LATER
		return new byte[32];
	}
}
