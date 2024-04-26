import java.util.ArrayList;
import java.security.MessageDigest;
import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;

public class Block {
    int index;
    long timestamp;
    byte[] previousHash=new byte[32];
    ArrayList<Transaction> transactions=new ArrayList<Transaction>();
    long nonce;
    byte[] thisblockHash;
    public byte[] blockAsByteArray() {
        ByteArrayOutputStream blockAsByteArray=new ByteArrayOutputStream();
        blockAsByteArray.write(index);
        blockAsByteArray.write(timestamp);
        blockAsByteArray.write(previousHash);
        ObjectOutputStream objectToByte=new ObjectOutputStream(blockAsByteArray); 
        try {
            for(int i=0; i<transactions.size(); i++) {
                objectToByte.writeObject(transactions.get(i));
            }
            objectToByte.flush();
            blockAsByteArray.write();
        } catch (Exception e) {
        }
        blockAsByteArray.write(nonce);
        return blockAsByteArray.toByteArray();
    }
    public byte[] hashBlock() {
    }
    public boolean checkHashZeros(byte[] hash, int numZeros) {
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
