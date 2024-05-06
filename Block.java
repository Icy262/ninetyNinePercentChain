import java.util.ArrayList;
import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.Serializable;

public class Block implements Serializable, MerkleTreeable {
	int index;
	long timestamp;
	long nonce=0; //index 12-20
	byte[] previousHash=new byte[32];
	ArrayList<Transaction> transactions=new ArrayList<Transaction>();
	public byte[] blockAsByteArray() {
		try {
			ByteArrayOutputStream blockAsByteArray=new ByteArrayOutputStream();
			DataOutputStream longWriter=new DataOutputStream(blockAsByteArray);
			blockAsByteArray.write(index);
			longWriter.writeLong(timestamp);
			longWriter.writeLong(nonce);
			longWriter.flush();
			blockAsByteArray.write(previousHash);
			ObjectOutputStream objectToByte=new ObjectOutputStream(blockAsByteArray); 
			for(int i=0; i<transactions.size(); i++) {
				objectToByte.writeObject(transactions.get(i));
			}
			objectToByte.flush();
			return blockAsByteArray.toByteArray();
		} catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}
	public byte[] hash() {
		return SHA256Hash.hash(blockAsByteArray());
	}
	public static boolean checkHashZeros(byte[] hash, int numZeros) {
		for(int i=0; i<numZeros; i++) {
			if(((hash[(int)(i/8)]>>(int)(i%8))&1)==1) {
				return false; //Doesn't have numZeros
			}
		}
		return true; //Has numZeros
	}
	public Block(int index, long timestamp, byte[] previousHash, ArrayList<Transaction> transactions) {
		this.index=index;
		this.timestamp=timestamp;
		this.previousHash=previousHash;
		this.transactions=transactions;
	}
}