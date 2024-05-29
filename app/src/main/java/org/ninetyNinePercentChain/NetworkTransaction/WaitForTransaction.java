package org.ninetyNinePercentChain.NetworkTransaction;

public class WaitForTransaction {
	private byte[] address;
	private int value;
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