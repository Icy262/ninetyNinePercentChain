package ninetyNinePercentChain.Block;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import ninetyNinePercentChain.Block.Hashing.MerkleTree;
import ninetyNinePercentChain.Block.Hashing.MerkleTreeable;
import ninetyNinePercentChain.Block.Hashing.Sign;
import ninetyNinePercentChain.Keys.KeyPairManager;
import ninetyNinePercentChain.Utils.ByteArray;
import ninetyNinePercentChain.Utils.SHA256Hash;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class Transaction implements Serializable, MerkleTreeable {
	private TransactionIn[] TIN; //Not in header
	private TransactionOut[] TOUT; //Not in header
	private byte[] merkleRoot; //Header
	private long timestamp=System.currentTimeMillis(); //Header
	private byte[][] signature; //Not in header. Contains the merkle root signed by each of the TIN private keys.
	/*
	Name: Transaction
	Description: Constructor. Sets the values of our TINs, TOUTs, and our timestamp
	Precondition: TIN and TOUT are not null and contain valid TransactionIns and TransactionOuts. timestamp is the number of milliseconds since midnight, January 1, 1970.
	Postcondition: TIN, TOUT, and timestamp initalized to the specified values
	*/
	public Transaction(TransactionIn[] TIN, TransactionOut[] TOUT, long timestamp) {
		this.TIN=TIN;
		this.TOUT=TOUT;
		this.timestamp=timestamp;
	}
	/*
	Name: hash
	Description: Converts the header to a byte array, hashes it, and returns it
	Precondition: All header values have been initialized
	Postcondition: SHA256 hash of the header returned. The same output value will always be returned for the same header values. 
	*/
	public byte[] hash() {
		return SHA256Hash.hash(headerAsByteArray());
	}
	/*
	Name: headerAsByteArray
	Description: Converts the header into a byte array in a deterministic manner
	Precondition: All values in header initialized
	Postcondition: header converted to byte array and returned
	*/
	public byte[] headerAsByteArray() {
		try {
			ByteArrayOutputStream headerAsByteArray=new ByteArrayOutputStream();
			DataOutputStream longWriter=new DataOutputStream(headerAsByteArray);
			longWriter.writeLong(timestamp);
			longWriter.flush();
			genMerkleRoot();
			headerAsByteArray.write(merkleRoot);
			return headerAsByteArray.toByteArray();
		} catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}
	/*
	Name: signTransaction
	Description: For each key in keyNames, sign the header with the private key. If there is only one key, and there are multiple TINs, we just sign the transaction multiple times with the same private key 
	Precondition: Each keyName in keyNames corresponds to a valid key in the keystore
	Postcondition: signatures is generated
	*/
	public void signTransaction(String[] keyNames) {
		signature=new byte[TIN.length][32];
		if(keyNames.length==1) {
			String bufferKeyName=keyNames[0];
			keyNames=new String[TIN.length];
			for(int i=0; i<keyNames.length; i++) {
				keyNames[i]=bufferKeyName;
			}
		}
		try {
			for(int i=0; i<TIN.length; i++) {
				signature[i]=Sign.privateKeySign(headerAsByteArray(), KeyPairManager.readKey(keyNames[i]).getPrivate());
			}
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	/*
	Name: genMerkleRoot
	Description: Converts the TIN to an ArrayList and generates the merkle root. It also converts the TOUT to an ArrayList and generates the merkle root. It merges the two merkle roots together and sets the value of the merkle root in the Transaction to this value.
	Precondition: The TIN and TOUT arrays have been initialized
	Postcondition: The merkle root field in the Transaction has been set
	*/
	private void genMerkleRoot() {
		merkleRoot=ByteArray.merge(new MerkleTree<TransactionIn>(new ArrayList<TransactionIn>(Arrays.asList(TIN))).genTree(), new MerkleTree<TransactionOut>(new ArrayList<TransactionOut>(Arrays.asList(TOUT))).genTree());
	}
	/*
	Name: getMerkleRoot
	Description: Gets the value of the merkleRoot
	Precondition: The merkleRoot has been generated
	Postcondition: The value of the merkleRoot field is returned
	*/
	public byte[] getMerkleRoot() {
		return merkleRoot;
	}
	/*
	Name: getSignature
	Description: returns the value of index index in the signature array
	Precondition: The signature array has been generated
	Postcondition: The value at index index in the signature array returned
	*/
	public byte[] getSignature(int index) {
		return signature[index];
	}
	/*
	Name: getTOUT
	Description: Returns the TransactionIn at index index in the TIN array
	Precondition: There is a index index in the TIN array
	Postcondition: TransactionIn at index index in the TIN array is returned
	*/
	public TransactionIn getTIN(int index) {
		return TIN[index];
	}
	/*
	Name: getTOUT
	Description: Returns the TransactionOut at index index in the TOUT array
	Precondition: There is an index index in the TOUT array
	Postcondition: TransactionOut at index index in the TOUT array is returned
	*/
	public TransactionOut getTOUT(int index) {
		return TOUT[index];
	}
	/*
	Name: getTINLength
	Description: Gets the length of the TIN array and returns it
	Precondition: TIN has been initialized
	Postcondition: The length of TIN is returned
	*/
	public int getTINLength() {
		return TIN.length;
	}
	/*
	Name: getTOUTLength
	Description: Gets the length of the TOUT array and returns it
	Precondition: TOUT has been initialized
	Postcondition: The length of TOUT is returned
	*/
	public int getTOUTLength() {
		return TOUT.length;
	}
	/*
	Name: getAllTIN
	Description: Returns the entire array of TINS
	Precondition: TIN has been initialized
	Postcondition: Returns the TIN array
	*/
	public TransactionIn[] getAllTIN() {
		return TIN;
	}
	/*
	Name: getAllTOUT
	Description: Returns the entire array of TOUTs
	Precondition: TOUT has been initialized
	Postcondition: Returns the TOUT array
	*/
	public TransactionOut[] getAllTOUT() {
		return TOUT;
	}
}