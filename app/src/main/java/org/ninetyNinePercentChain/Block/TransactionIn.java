package org.ninetyNinePercentChain.Block;
import java.io.ByteArrayOutputStream;
import java.security.PrivateKey;

import org.ninetyNinePercentChain.Block.Hashing.MerkleTreeable;
import org.ninetyNinePercentChain.Block.Hashing.Sign;
import org.ninetyNinePercentChain.Utils.BlockFile;
import org.ninetyNinePercentChain.Utils.SHA256Hash;

public class TransactionIn implements MerkleTreeable {
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
	public void sign(PrivateKey key) {
		privateKeySignature=Sign.privateKeySign(toByteArray(), key);
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
	public int getValue() {
		Block block=BlockFile.readBlock(previousOutBlock); //The block that stores the previous output transaction
		Transaction transaction=block.getTransaction(previousOutTransaction); //The transaction the holds the previous output transaction
		return transaction.getTOUT(previousOutOutputNumber).getValue();
	}
}