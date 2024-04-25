import java.util.ArrayList;

public class Block implements Serializable {
    int index;
    long timestamp;
    byte[] previousHash=new byte[32];
    ArrayList<Transaction> transactions=new ArrayList<Transaction>();
    int nonce;
    byte[] thisblockHash=new byte[32];
}
