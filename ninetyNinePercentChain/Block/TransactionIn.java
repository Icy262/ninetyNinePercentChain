package ninetyNinePercentChain.Block;
import java.io.ByteArrayOutputStream;
import java.security.PrivateKey;

import ninetyNinePercentChain.Block.Hashing.MerkleTreeable;
import ninetyNinePercentChain.Block.Hashing.Sign;
import ninetyNinePercentChain.Utils.BlockFile;
import ninetyNinePercentChain.Utils.SHA256Hash;

/*
 * TransactionIn is used to define where the tokens being transfered by a Transaction should be taken from. It contains a reference to the location of the TOUT it is spending, along with a signature using the private key that coresponds to the public key specified in the TOUT.
 */

public class TransactionIn implements MerkleTreeable {
	private int previousOutBlock; //Block number of previous transaction
	private int previousOutTransaction; //Transaction number of previous transaction
	private int previousOutOutputNumber; //The output number of the previous trasaction
	private byte[] privateKeySignature; //Signature with coresponding private key of transaction output
	/*
	Name: hash
	Description: Converts the header to a byte array and hashes it with SHA256
	Precondition: All header values have been initialized
	Postcondition: Header converted to a byte array and hashed with SHA256. The header values should always produce the same hash.
	*/
	public byte[] hash() {
		return SHA256Hash.hash(toByteArray()); //Converts the TIN to a byte array and hashes it
	}
	/*
	Name: toByteArray
	Description: Converts the header to a byte array and returns it
	Precondition: All header values have been initialized
	Postcondition: Header converted to a byte array in a deterministic manner.
	*/
	public byte[] toByteArray() {
		ByteArrayOutputStream headerAsByteArray=new ByteArrayOutputStream(); //ByteArrayOutputStream. Makes it easy to combine all the ints into a single byte[]
		headerAsByteArray.write(previousOutBlock);
		headerAsByteArray.write(previousOutTransaction);
		headerAsByteArray.write(previousOutOutputNumber);
		return headerAsByteArray.toByteArray(); //Writes the stream to a byte array and returns it
	}
	/*
	Name: sign
	Description: Sets the value of privateKeySignature. Signs the header with the private key provided.
	Precondition: All header values are have been initialized. keyName is the name of a valid key in the keystore
	Postcondition: privateKeySignature value set.
	*/
	public void sign(String keyName) {
		privateKeySignature=Sign.privateKeySign(toByteArray(), keyName); //Signs the TransactionIn with the private key named keyName
	}
	/*
	Name: sign
	Description: Sets the value of privateKeySignature. Signs the header with the private key provided.
	Precondition: All header values are have been initialized. keyName is the name of a valid key in the keystore
	Postcondition: privateKeySignature value set.
	*/
	public void sign(PrivateKey key) {
		privateKeySignature=Sign.privateKeySign(toByteArray(), key); //Signs the TransactionIn with the private key
	}
	/*
	Name: getPreviousOutBlock
	Description: Returns the value of the preivousOutBlock field
	Precondition: previousOutBlock has been initialized
	Postcondition: Value of previousOutBlock returned
	*/
	public int getPreviousOutBlock() {
		return previousOutBlock;
	}
	/*
	Name: getPreviousOutTransaction
	Description: Returns the value of the previousOutTransaction field
	Precondition: previousOutTransaction has been initalized
	Postcondition: Value of previousOutTransaction returned
	*/
	public int getPreviousOutTransaction() {
		return previousOutTransaction;
	}
	/*
	Name: getPreviousOutOutputNumber
	Description: Returns the value of the previousOutOutputNumber field
	Precondition: previousOutOutputNumber has been initalized
	Postcondition: Value of previousOutOutputNumber returned
	*/
	public int getPreviousOutOutputNumber() {
		return previousOutOutputNumber;
	}
	/*
	Name: getPrivateKeySignature
	Description: Returns the value of the privateKeySignature
	Precondition: The privateKeySignature has been generated
	Postcondition: Value of privateKeySignature returned
	*/
	public byte[] getPrivateKeySignature() { 
		return privateKeySignature;
	}
	/*
	Name: TransactionIn
	Description: Constructor. Allows us to set the block, transaction, and output number that this TIN points to
	Precondition: Previous out block is the index of the block that we referece. Previous out transaction is the index of the transaction storing our TOUT in the block. Previous out output number is the index of the TOUT in the list of TOUTs in the transaction.
	Postcondition: Constructs the object and sets the values in the header
	*/
	public TransactionIn(int previousOutBlock, int previousOutTransaction, int previousOutOutputNumber) {
		this.previousOutBlock=previousOutBlock; //Index of the block that has the TOUT
		this.previousOutTransaction=previousOutTransaction; //Index of the Transaction that has the TOUT
		this.previousOutOutputNumber=previousOutOutputNumber; //Index of the TOUT we reference
	}
	/*
	Name: getValue
	Description: Gets the value of the TOUT that this TIN references
	Precondition: previousOutBlock, previousOutTransaction, and previousOutOutput number are all initalized and valid
	Postcondition: The value of the TOUT that this TIN references returned
	*/
	public int getValue() {
		return BlockFile.getTOUT(previousOutBlock, previousOutTransaction, previousOutOutputNumber).getValue(); //Returns the value of the TOUT we reference 
	}
}