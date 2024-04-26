import java.util.ArrayList;
import java.io.Serializable;

public class HashBlock implements Serializable {
    int index;
    long timestamp;
    byte[] previousHash=new byte[32];
    ArrayList<Transaction> transactions=new ArrayList<Transaction>();
    long nonce=0;
    public HashBlock(int index, long timestamp, byte[] previousHash, ArrayList<Transaction> transactions) {
        this.index=index;
        this.timestamp=timestamp;
        this.previousHash=previousHash;
        this.transactions=transactions;
    }
}
