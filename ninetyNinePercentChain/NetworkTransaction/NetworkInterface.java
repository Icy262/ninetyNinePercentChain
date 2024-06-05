package ninetyNinePercentChain.NetworkTransaction;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import ninetyNinePercentChain.Block.Block;
import ninetyNinePercentChain.Block.Transaction;
import ninetyNinePercentChain.Block.TransactionIn;
import ninetyNinePercentChain.Block.TransactionOut;
import ninetyNinePercentChain.Block.Hashing.FindBlockHashManager;
import ninetyNinePercentChain.Keys.KeyPairManager;
import ninetyNinePercentChain.Network.DNS.QueryDNS;
import ninetyNinePercentChain.Network.DNS.RegisterDNS;
import ninetyNinePercentChain.Network.InterNode.Read.NetworkReceiveHandler;
import ninetyNinePercentChain.Network.InterNode.Sync.SyncChainResponseManager;
import ninetyNinePercentChain.Network.InterNode.Write.NetworkSendManager;
import ninetyNinePercentChain.Network.KeepAlive.KeepAliveManager;
import ninetyNinePercentChain.Network.KeepAlive.KeepAliveResponse;
import ninetyNinePercentChain.Utils.BlockFile;

public class NetworkInterface {
	private static QueryDNS queryDNS;
	private static KeepAliveResponse keepAliveResponse;
	private static KeepAliveManager keepAliveManager;
	private static NetworkReceiveHandler networkReceiveHandler;
	private static SyncChainResponseManager syncChainResponseManager;
	/*
	Name: setup
	Description: When we are running a non hashing node, that will be used to create and recieve transactions only, as part of a larger project, this will be used instead of main. Initializes the class to have all the connections and threads necessary.
	Precondition: None
	Postcondition: Program is ready to create and process transactions
	*/
	public static void setup() {
		queryDNS=new QueryDNS(); //Creates a QueryDNS object
		keepAliveResponse=new KeepAliveResponse(); //Creates a KeepAliveResponse object
		keepAliveManager=new KeepAliveManager(); //Creates a KeepAliveManager object
		networkReceiveHandler=new NetworkReceiveHandler(false); //Creates a NetworkRecieveHandler object with hashing flag set to false. This will make it so that this node does not process transactions, but instead it redirects them to the WaitForTransactionManager
		syncChainResponseManager=new SyncChainResponseManager(); //Creates a SyncChainResponseManager object
		new RegisterDNS().start(); //Starts the RegisterDNS thread. This will tell the DNS server that we are a node and we are online.
		queryDNS.start(); //Starts the QueryDNS thread. This will query the DNS server for new IP adresses every 10 minutes or if there was no IPs returned last time, 1 second.
		keepAliveResponse.start(); //Starts the KeepAliveResponse thread. This will respond to any incoming keep alive requests by starting a new KeepAliveResponse thread.
		keepAliveManager.start(); //Starts the KeepAliveManager thread. This will send out keep alive messages to all the IPs in our list every five minutes. If they don't accept the connection within 10 seconds, we delete their ip from our list.
		networkReceiveHandler.start(); //Starts the NetworkRecieveHandler thread with the hashing flag set to true. This will accept any incoming data and direct it to the hashing manager.
		syncChainResponseManager.start(); //Starts the SyncChainReponseManager. This will sync our copy of the blockchain with any new IPs that we add to our list.
	}
	/*
	Name: cleanup
	Description: Stops everything and cleans up after the setup method. To be used when stopping the program
	Precondition: Setup has already been called. Cleanup has not been called yet
	Postcondition: Threads stopped and cleanup finished
	*/
	public static void cleanup() {
		queryDNS.stopThread(); //Stops the queryDNS thread
		keepAliveResponse.stopThread(); //Stops the keepAliveResponse thread
		keepAliveManager.stopThread(); //Stops the keepAliveManager thread
		networkReceiveHandler.stopThread(); //Stops the networkReceiveHandler thread
		syncChainResponseManager.stopThread(); //Stops the syncChainRepsponseManager
		NetworkSendManager.stopThreads(); //Stops all write threads
	}
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
						//ADD CHECK FOR ALREADY SPENT TRANSACTION
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