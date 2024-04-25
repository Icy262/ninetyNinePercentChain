import java.util.ArrayList;

public class Block {
    int index;
    long timestamp;
    byte[] previousHash=new byte[32];
    ArrayList<Transaction> transactions=new ArrayList<Transaction>();
    long nonce;
    byte[] thisblockHash;
    public byte[] hashBlock() {
    }
    public boolean checkHashZeros(byte[] hash, int numZeros) {
        for(int i=0; i<numZeros; i++) {
            if((hash[i]>>(i-1)) & 1)==1) {
                return false
            }
        }
        return true;
    }
}
