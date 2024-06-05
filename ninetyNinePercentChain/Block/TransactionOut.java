package ninetyNinePercentChain.Block;

import ninetyNinePercentChain.Block.Hashing.MerkleTreeable;
import ninetyNinePercentChain.Utils.SHA256Hash;

import java.io.ByteArrayOutputStream;

public class TransactionOut implements MerkleTreeable {
	private byte[] nextTransactionPublicKey; //Public key that corresponds to the private key that the next transaction must be signed with
	private int value; //Value of transaction
	/*
	Name: hash
	Description: Converts the header to a byte array and hashes it with SHA256
	Precondition: All values in header initialized
	Postcondition: Hash returned. For the same header values, the same output should always be produced
	*/
	public byte[] hash() {
		return SHA256Hash.hash(toByteArray());
	}
	/*
	Name: toByteArray
	Description: Converts the header to a byte array in a deterministic manner.
	Precondition: All header values initalized
	Postcondition: The header is converted to a byte array in a consistent manner and returned
	*/
	public byte[] toByteArray() {
		ByteArrayOutputStream headerAsByteArray=new ByteArrayOutputStream(); //ByteArrayOutputStream. Allows us to easily combine ints and byte[].
		headerAsByteArray.writeBytes(nextTransactionPublicKey);
		headerAsByteArray.write(value);
		return headerAsByteArray.toByteArray(); //Writes the output stream to a byte array and returns it
	}
	/*
	Name: getValue
	Description: Returns the value of the value field
	Precondition: The value field has been initalized
	Postcondition: Value of value returned
	*/
	public int getValue() {
		return value;
	}
	/*
	Name: getNextTransactionPublicKey
	Description: Returns the value of nextTransactionPublicKey
	Precondition: nextTransactionPublicKey has been initialized
	Postcondition: Value of nextTransactionPublicKey has been returned
	*/
	public byte[] getNextTransactionPublicKey() {
		return nextTransactionPublicKey;
	}
	/*
	Name: TransactionOut
	Description: Constructor. Allows us to set values for value and nextTransactionPublicKey
	Precondition: Value is a positive number. nextTransactionPublicKey is a valid byte representation of a PublicKey object in java
	Postcondition: All values set
	*/
	public TransactionOut(byte[] nextTransactionPublicKey, int value) {
		this.nextTransactionPublicKey=nextTransactionPublicKey; //The address we are sending the token to
		this.value=value; //The amount of tokens to send
	}
}