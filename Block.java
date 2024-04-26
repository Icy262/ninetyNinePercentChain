import java.util.ArrayList;
import java.security.MessageDigest;
import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;

public class Block {
    int index;
    long timestamp;
    byte[] previousHash=new byte[32];
    ArrayList<Transaction> transactions=new ArrayList<Transaction>();
    byte[] thisblockHash;
    transient HashBlock hashBlock=new HashBlock(index, timestamp, previousHash, transactions);
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
}