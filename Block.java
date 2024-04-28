import java.util.ArrayList;
import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;;

public class Block {
    int index;
    long timestamp;
    byte[] previousHash=new byte[32];
    ArrayList<Transaction> transactions=new ArrayList<Transaction>();
    long nonce;
    byte[] thisblockHash;
    public byte[] blockAsByteArray() {
        try {
            ByteArrayOutputStream blockAsByteArray=new ByteArrayOutputStream();
            DataOutputStream longWriter=new DataOutputStream(blockAsByteArray);
            blockAsByteArray.write(index);
            longWriter.writeLong(timestamp);
            blockAsByteArray.write(previousHash);
            ObjectOutputStream objectToByte=new ObjectOutputStream(blockAsByteArray); 
            for(int i=0; i<transactions.size(); i++) {
                objectToByte.writeObject(transactions.get(i));
            }
            objectToByte.flush();
            longWriter.writeLong(nonce);
            return blockAsByteArray.toByteArray();
        } catch(Exception e) {
            System.out.println(e);
            return null;
        }
    }
    public static byte[] hashBlock(byte[] blockAsByteArray) {
        return SHA256Hash.hash(blockAsByteArray);
    }
    public static boolean checkHashZeros(byte[] hash, int numZeros) {
        for(int i=0; i<numZeros; i++) {
            if(((hash[i]>>(i-1)) & 1)==1) {
                return false;
            }
        }
        return true;
    }
    public Block(int index, long timestamp, byte[] previousHash, ArrayList<Transaction> transactions) {
        this.index=index;
        this.timestamp=timestamp;
        this.previousHash=previousHash;
        this.transactions=transactions;
    }
}
