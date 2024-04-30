import java.io.Serializable;

public class Transaction implements Serializable {
	TransactionIn[] TIN;
	TransactionOut[] TOUT;
	long timestamp=System.currentTimeMillis();

	public Transaction(TransactionIn[] TIN, TransactionOut[] TOUT) {
		this.TIN=TIN;
		this.TOUT=TOUT;
	}
}
