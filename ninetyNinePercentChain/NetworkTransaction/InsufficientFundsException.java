package ninetyNinePercentChain.NetworkTransaction;

class InsuffientFundsException extends Exception {
	/*
	Name: InsufficientFunsException
	Description: Thrown when there are not enough funds in an address to make a transaction
	Precondition: None
	Postcondition: None
	*/
	public InsuffientFundsException() {
		super(); //We don't need to do anything special, we just need to be able to tell the caller why the transaction maker failed.
	}
}