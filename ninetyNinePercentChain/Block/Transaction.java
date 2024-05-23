package ninetyNinePercentChain.Block;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import ninetyNinePercentChain.Block.Hashing.MerkleTree;
import ninetyNinePercentChain.Block.Hashing.MerkleTreeable;
import ninetyNinePercentChain.Block.Hashing.Sign;
import ninetyNinePercentChain.Keys.KeyPairManager;
import ninetyNinePercentChain.Utils.ByteArray;
import ninetyNinePercentChain.Utils.SHA256Hash;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class Transaction implements Serializable, MerkleTreeable {
	private TransactionIn[] TIN; //Not in header
	private TransactionOut[] TOUT; //Not in header
	private byte[] merkleRoot; //Header
	private long timestamp=System.currentTimeMillis(); //Header
	private byte[][] signature; //Not in header. Contains the merkle root signed by each of the TIN private keys.

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
			genMerkleRoot();
			headerAsByteArray.write(merkleRoot);
			return headerAsByteArray.toByteArray();
		} catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}
	public void signTransaction(String[] keyNames) {
		try {
			for(int i=0; i<TIN.length; i++) {
				signature[i]=Sign.privateKeySign(headerAsByteArray(), KeyPairManager.readKey(keyNames[i]).getPrivate());
			}
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	private void genMerkleRoot() {
		merkleRoot=ByteArray.merge(new MerkleTree<TransactionIn>(new ArrayList<TransactionIn>(Arrays.asList(TIN))).genTree(), new MerkleTree<TransactionOut>(new ArrayList<TransactionOut>(Arrays.asList(TOUT))).genTree());
	}
	public byte[] getMerkleRoot() {
		return merkleRoot;
	}
	public byte[] getSignature(int index) {
		return signature[index];
	}
	public TransactionIn getTransactionIn(int index) {
		return TIN[index];
	}
	public TransactionOut getTransactionOut(int index) {
		return TOUT[index];
	}
	public int getTINLength() {
		return TIN.length;
	}
	public int getTOUTLength() {
		return TOUT.length;
	}
	public TransactionOut getTOUT(int index) {
		return TOUT[index];
	}
	public TransactionIn[] getAllTIN() {
		return TIN;
	}
	public TransactionOut[] getAllTOUT() {
		return TOUT;
	}
}