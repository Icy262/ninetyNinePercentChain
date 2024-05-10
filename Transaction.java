import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class Transaction implements Serializable, MerkleTreeable {
	TransactionIn[] TIN; //Not in header
	TransactionOut[] TOUT; //Not in header
	byte[] merkleRoot; //Header
	long timestamp=System.currentTimeMillis(); //Header
	byte[] signature; //Header

	public Transaction(TransactionIn[] TIN, TransactionOut[] TOUT, long timestamp) {
		this.TIN=TIN;
		this.TOUT=TOUT;
		this.timestamp=timestamp;
	}
	public byte[] hash() {
		return SHA256Hash.hash(headerAsByteArray());
	}
	public byte[] headerAsByteArray() {
		try {
			ByteArrayOutputStream headerAsByteArray=new ByteArrayOutputStream();
			DataOutputStream longWriter=new DataOutputStream(headerAsByteArray);
			longWriter.writeLong(timestamp);
			longWriter.flush();
			headerAsByteArray.write(merkleRoot);
			return headerAsByteArray.toByteArray();
		} catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}
	public void signTransaction(String keyName) {
		signature=Sign.privateKeySign(headerAsByteArray(), keyName);
	}
	public void genMerkleRoot() {
		merkleRoot=ByteArray.merge(new MerkleTree<TransactionIn>(new ArrayList<TransactionIn>(Arrays.asList(TIN))).genTree(), new MerkleTree<TransactionOut>(new ArrayList<TransactionOut>(Arrays.asList(TOUT))).genTree());
	}
}