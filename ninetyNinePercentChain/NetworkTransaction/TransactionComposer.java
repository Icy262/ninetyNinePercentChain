package ninetyNinePercentChain.NetworkTransaction;

import ninetyNinePercentChain.Block.Block;
import ninetyNinePercentChain.Block.Transaction;
import ninetyNinePercentChain.Block.TransactionIn;
import ninetyNinePercentChain.Block.TransactionOut;
import ninetyNinePercentChain.Block.Hashing.CheckValidity;
import ninetyNinePercentChain.Block.Hashing.FindBlockHashManager;
import ninetyNinePercentChain.Keys.KeyPairManager;
import ninetyNinePercentChain.Network.InterNode.Write.NetworkSendManager;
import ninetyNinePercentChain.Utils.BlockFile;

import java.security.KeyPair;
import java.security.PublicKey;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

/*
 * TransactionComposer lets us create a transaction of a given value from an address to an address. It also contains methods to find TINs for these transactions and methods to find the value of an Array of TINs
 */

public class TransactionComposer {
	/*
	Name: createTransaction
	Description: Creates a transaction of a certain value, spending tokens from a certain address, to a certain address
	Precondition: keyName is the name of a key, outKey is the reciever's address. value is not negative or zero.
	Postcondition: Transaction created and sent, or insufficient funds exception thrown
	*/
	public static void createTransaction(int value, String keyName, PublicKey outKey) throws InsuffientFundsException {
		KeyPair keyPair=KeyPairManager.readKey(keyName); //Gets the keypair of the sender
		byte[] keyAsByteArray=keyPair.getPublic().getEncoded(); //Gets the public key of the sender's keypair and encodes it as a byte array
		TransactionIn[] transactionIns=findInputs(keyAsByteArray, value, keyPair.getPrivate()); //Creates the TIN list
		int totalValue=findTotalValue(transactionIns); //Gets the value of the TIN. This will be more than or equal to the value of variable value
		TransactionOut[] transactionOuts; //Scope
		if(totalValue==value) { //If we have the exact right amount of input,
			transactionOuts=new TransactionOut[] {new TransactionOut(outKey.getEncoded(), value)}; //Direct all input to the output
		} else { //If we have extra input,
			transactionOuts=new TransactionOut[] {new TransactionOut(outKey.getEncoded(), value), new TransactionOut(keyPair.getPublic().getEncoded(), totalValue-value)}; //Direct value amount to the reciever and surplus back to ourselves
		}
		Transaction transaction=new Transaction(transactionIns, transactionOuts, System.currentTimeMillis()); //Creates the transaction with out TIN and TOUTs
		String[] keys=new String[transactionIns.length]; //Creates a String[] the length of the TIN list.
		for(int i=0; i<keys.length; i++) { //Duplicate the TIN private key TIN.length number of times. This is because the validation checks expect one signature for each TIN, even if they are the same
			keys[i]=keyName;
		}
		transaction.signTransaction(keys); //Signs the Transaction for each of the private keys
		NetworkSendManager.addToQueue((Object) transaction); //Adds the Transaction to the send queue. This makes it get sent out over the network.
		FindBlockHashManager.addTransaction(transaction); //Gives the Transaction to the FindBlockHashManager so that it can be hashed.
	}
	/*
	Name: createTransaction
	Description: Same as other createTransaction, but it allows for a byte[] to be passed as output address
	Precondition: None
	Postcondition: Transaction created and sent, or insufficient funds exception thrown
	*/
	public static void createTransaction(int value, String keyName, byte[] outKey) throws InsuffientFundsException {
		try {
			PublicKey outKeyAsObject=KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(outKey)); //Converts the byte[] encoding of the reciever's public key to a PublicKey object
			createTransaction(value, keyName, outKeyAsObject); //Calls the other createTransaction method 
		} catch(NoSuchAlgorithmException e) {
			System.out.println(e);
		} catch(InvalidKeySpecException e) {
			System.out.println(e);
		}
	}
	/*
	Name: findInputs
	Description: Scans the blockchain for all TOUTs owned by an address until we find enough that we meet the value required. It compiles these into an array of TINs and returns it
	Precondition: keyAsByteArray is the address we are searching for. Value is the total token value. privateKey is the private key coresponding to the address we are searcching for
	Postcondition: TransactionIn[] generated and sent or insufficient funds exception thrown
	*/
	private static TransactionIn[] findInputs(byte[] keyAsByteArray, int value, PrivateKey privateKey) throws InsuffientFundsException {
		ArrayList<TransactionIn> transactionList=new ArrayList<TransactionIn>(); //List of TINs
		int addressBalance=0; //Accumulator for balance of the TINs. This lets us track when to stop
		for(int i=BlockFile.getHighestIndex(); i>=0; i--) { //For each Block, (we iterate negatively because this will make spending recent TOUTs faster. There will be a higher concentration of unspent TOUTs towards the most recent blocks)
			Block block=BlockFile.readBlock(i); //Buffer for the block. Prevents needing to read it multiple times
			for(int ii=0; ii<block.getNumTransactions(); ii++) { //For each transaction in the block,
				Transaction transaction=block.getTransaction(ii); //Buffer for the transaction. Prevents needing to get it multiple times.
				for(int iii=0; i<transaction.getTOUTLength(); iii++) { //For each TOUT in the transaction,
					if(Arrays.equals(transaction.getTOUT(iii).getNextTransactionPublicKey(), keyAsByteArray)) { //If the TOUT is giving funds to our address,
						if(!CheckValidity.isTOUTSpent(i, ii, iii)) { //If the TOUT is unspent,
							transactionList.add(new TransactionIn(i, ii, iii)); //Ad a new TIN referencing this TOUT
							transactionList.get(transactionList.size()-1).sign(privateKey); //Sign the new TIN with our private key
							addressBalance+=transaction.getTOUT(iii).getValue(); //Add the amount of the TOUT to the accumulator
							if(addressBalance>=value) { //If we have enough funds,
								return (TransactionIn[]) transactionList.toArray(); //Return our list of TINs
							} //If not, keep looking
						}
					}
				}
			}
		}
		throw new InsuffientFundsException(); //We have checked every TOUT in every Transaction in every Block, and we don't have enough funds
	}
	/*
	Name: findTotalValue
	Description: Finds the sum of the values of an array of TINs
	Precondition: All TINs are valid and point to blocks that we have in our blockchain directory
	Postcondition: Total value of TINs returned
	*/
	private static int findTotalValue(TransactionIn[] transactionIns) {
		int totalValue=0; //Accumulator for value
		for(int i=0; i<transactionIns.length; i++) { //For each TIN,
			totalValue+=transactionIns[i].getValue(); //Add the value of the TOUT that the TIN references to the accumulator
		}
		return totalValue; //Return the total value of the TIN.
	}
}