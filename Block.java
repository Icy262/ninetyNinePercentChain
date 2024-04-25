import java.util.ArrayList;

public class Block {
    byte[] previousHash=new byte[32];
    ArrayList<Transaction> transactions=new ArrayList<Transaction>();
}