package ninetyNinePercentChain.NetworkTransaction;

public class WaitForTransaction {
	private byte[] address;
	private int value;
	//NEED TO ADD A TIMESTAMP
	//WE SHOULD CHECK THAT THE TRANSACTION WAS CREATED AFTER A CERTAIN TIME, BUT BEFORE NOW (NOT IN THE FUTURE).
	public WaitForTransaction(byte[] address, int value) {
		this.address=address;
		this.value=value;
		WaitForTransactionManager.addWait(this);
	}
	public void startWait() {
		try {
			wait();
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	public byte[] getAddress() {
		return address;
	}
	public int getValue() {
		return value;
	}
}