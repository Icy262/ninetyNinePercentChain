package ninetyNinePercentChain.NetworkTransaction;

/*
 * This object defines the conditions that must be met for the thread to stop waiting. It includes sender, reciever, and value of the transaction.
 */

public class WaitForTransaction {
	private byte[] sendAddress; //Address sending
	private byte[] recieveAddress; //Address recieving
	private int value; //Value
	//NEED TO ADD A TIMESTAMP
	//WE SHOULD CHECK THAT THE TRANSACTION WAS CREATED AFTER A CERTAIN TIME, BUT BEFORE NOW (NOT IN THE FUTURE).
	/*
	Name: WaitForTransaction
	Description: Constructor. Sets what address we are monitoring for a transaction to a certain address for a certain value
	Precondition: None
	Postcondition: Waits until transaction is included in block we recieve
	*/
	public WaitForTransaction(byte[] sendAddress, byte[] recieveAddress, int value) {
		this.sendAddress=sendAddress; //Address sending tokens
		this.recieveAddress=recieveAddress; //Address recieving the tokens
		this.value=value; //Amount of tokens
		WaitForTransactionManager.addWait(this); //Takes this object and adds it to the waiting transactions list
	}
	/*
	Name: startWait
	Description: Makes the thread wait. It will get notified by the manager
	Precondition: None
	Postcondition: Thread waits
	*/
	public void startWait() {
		try {
			wait(); //Waits. This will block the calling method until we are awakened again.
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	/*
	Name: getSendAddress
	Description: Returns the value of sendAddress
	Precondition: sendAddress initialized
	Postcondition: Returns the value of sendAddress
	*/
	public byte[] getSendAddress() {
		return sendAddress;
	}
	/*
	Name:getRecieveAddress
	Description: Returns the value of recieveAddress
	Precondition: recieveAddress initialized
	Postcondition: Returns the value of recieveAddress
	*/
	public byte[] getRecieveAddress() {
		return recieveAddress;
	}
	/*
	Name: getValue
	Description: Returns the value of value
	Precondition: value has been initialized
	Postcondition: Value of value returned
	*/
	public int getValue() {
		return value;
	}
}