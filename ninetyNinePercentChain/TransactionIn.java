package ninetyNinePercentChain;
import java.io.ByteArrayOutputStream;

class TransactionIn implements MerkleTreeable {
	private int previousOutBlock; //Block number of previous transaction
	private int previousOutTransaction; //Transaction number of previous transaction
	private int previousOutOutputNumber; //The output number of the previous trasaction
	private byte[] privateKeySignature; //Signature with coresponding private key of transaction output
	public byte[] hash() {
		return SHA256Hash.hash(toByteArray());
	}
	public byte[] toByteArray() {
		ByteArrayOutputStream headerAsByteArray=new ByteArrayOutputStream();
		headerAsByteArray.write(previousOutBlock);
		headerAsByteArray.write(previousOutTransaction);
		headerAsByteArray.write(previousOutOutputNumber);
		return headerAsByteArray.toByteArray();
	}
	public void sign(String keyName) {
		privateKeySignature=Sign.privateKeySign(toByteArray(), keyName);
	}
	public int getPreviousOutBlock() {
		return previousOutBlock;
	}
	public int getPreviousOutTransaction() {
		return previousOutTransaction;
	}
	public int getPreviousOutOutputNumber() {
		return previousOutOutputNumber;
	}
	public byte[] getPrivateKeySignature() {
		return privateKeySignature;
	}
	public TransactionIn(int previousOutBlock, int previousOutTransaction, int previousOutOutputNumber) {
		this.previousOutBlock=previousOutBlock;
		this.previousOutTransaction=previousOutTransaction;
		this.previousOutOutputNumber=previousOutOutputNumber;
	}
}