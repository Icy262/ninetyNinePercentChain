package org.ninetyNinePercentChain.Block;
import java.util.ArrayList;

import org.ninetyNinePercentChain.Block.Hashing.MerkleTree;
import org.ninetyNinePercentChain.Block.Hashing.MerkleTreeable;
import org.ninetyNinePercentChain.Utils.SHA256Hash;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.Serializable;

public class Block implements Serializable, MerkleTreeable {
	private int index;
	private long timestamp;
	private long nonce=0; //index 12-20
	private byte[] previousHash=new byte[32];
	private byte[] merkleRoot;
	private ArrayList<Transaction> transactions=new ArrayList<Transaction>(); //Body
	public byte[] headerAsByteArray() {
		try {
			ByteArrayOutputStream headerAsByteArray=new ByteArrayOutputStream();
			DataOutputStream longWriter=new DataOutputStream(headerAsByteArray);
			headerAsByteArray.write(index);
			longWriter.writeLong(timestamp);
			longWriter.writeLong(nonce);
			longWriter.flush();
			headerAsByteArray.write(previousHash);
			headerAsByteArray.write(merkleRoot);
			return headerAsByteArray.toByteArray();
		} catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}
	public byte[] hash() {
		return SHA256Hash.hash(headerAsByteArray());
	}
	public static boolean checkHashZeros(byte[] hash, int numZeros) {
		for(int i=0; i<numZeros; i++) {
			if(((hash[(int)(i/8)]>>(int)(i%8))&1)==1) {
				return false; //Doesn't have numZeros
			}
		}
		return true; //Has numZeros
	}
	public void genMerkleRoot() {
		merkleRoot=new MerkleTree<Transaction>(transactions).genTree();
	}
	public Block(int index, long timestamp, byte[] previousHash, ArrayList<Transaction> transactions) {
		this.index=index;
		this.timestamp=timestamp;
		this.previousHash=previousHash;
		this.transactions=transactions;
	}
	public void addTransaction(Transaction toAdd) {
		transactions.add(toAdd);
	}
	public int getIndex() {
		return index;
	}
	public byte[] getMerkleRoot() {
		return getMerkleRoot();
	}
	public Transaction getTransaction(int index) {
		return transactions.get(index);
	}
	public int getNumTransactions() {
		return transactions.size();
	}
	public byte[] getPreviousHash() {
		return previousHash;
	}
	public ArrayList<Transaction> getAllTransactions() {
		return transactions;
	}
	public long getNonce() {
		return nonce;
	}
	public void setNonce(long nonce) {
		this.nonce = nonce;
	}
}